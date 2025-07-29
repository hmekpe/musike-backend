#!/bin/bash

# Database backup script for Musike backend
# This script creates automated backups of the PostgreSQL database

set -e

# Configuration
BACKUP_DIR="/backups"
DB_NAME="Musike"
DB_USER="postgres"
DB_HOST="postgres"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
BACKUP_FILE="${BACKUP_DIR}/musike_backup_${TIMESTAMP}.sql"
RETENTION_DAYS=30

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Log function
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}" >&2
}

warning() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

# Create backup directory if it doesn't exist
mkdir -p "$BACKUP_DIR"

# Function to create backup
create_backup() {
    log "Starting database backup..."
    
    # Create backup using pg_dump
    if pg_dump -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME" -f "$BACKUP_FILE" --verbose; then
        log "Backup created successfully: $BACKUP_FILE"
        
        # Compress the backup
        gzip "$BACKUP_FILE"
        log "Backup compressed: ${BACKUP_FILE}.gz"
        
        # Get file size
        FILE_SIZE=$(du -h "${BACKUP_FILE}.gz" | cut -f1)
        log "Backup size: $FILE_SIZE"
        
        return 0
    else
        error "Backup failed!"
        return 1
    fi
}

# Function to clean old backups
cleanup_old_backups() {
    log "Cleaning up backups older than $RETENTION_DAYS days..."
    
    # Find and remove old backup files
    find "$BACKUP_DIR" -name "musike_backup_*.sql.gz" -type f -mtime +$RETENTION_DAYS -delete
    
    log "Cleanup completed"
}

# Function to list backups
list_backups() {
    log "Available backups:"
    ls -lh "$BACKUP_DIR"/musike_backup_*.sql.gz 2>/dev/null || warning "No backup files found"
}

# Function to check database connectivity
check_db_connection() {
    log "Checking database connectivity..."
    
    if pg_isready -h "$DB_HOST" -U "$DB_USER" -d "$DB_NAME"; then
        log "Database connection successful"
        return 0
    else
        error "Database connection failed!"
        return 1
    fi
}

# Main execution
main() {
    log "=== Musike Database Backup Script ==="
    
    # Check if we're running in a container
    if [ -f /.dockerenv ]; then
        log "Running in Docker container"
    fi
    
    # Check database connection
    if ! check_db_connection; then
        error "Cannot connect to database. Exiting."
        exit 1
    fi
    
    # Create backup
    if create_backup; then
        # Clean up old backups
        cleanup_old_backups
        
        # List available backups
        list_backups
        
        log "Backup process completed successfully"
    else
        error "Backup process failed"
        exit 1
    fi
}

# Handle script arguments
case "${1:-backup}" in
    "backup")
        main
        ;;
    "list")
        list_backups
        ;;
    "cleanup")
        cleanup_old_backups
        ;;
    "test")
        check_db_connection
        ;;
    *)
        echo "Usage: $0 {backup|list|cleanup|test}"
        echo "  backup   - Create a new backup (default)"
        echo "  list     - List available backups"
        echo "  cleanup  - Clean up old backups"
        echo "  test     - Test database connection"
        exit 1
        ;;
esac