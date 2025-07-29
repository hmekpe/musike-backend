# 🐳 Musike Backend - Docker Production Deployment

This document provides comprehensive instructions for deploying the Musike backend using Docker in production environments.

## 📋 Prerequisites

- Docker Engine 20.10+
- Docker Compose 2.0+
- At least 4GB RAM available
- 10GB free disk space
- SSL certificates (for HTTPS)

## 🏗️ Architecture Overview

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Nginx (SSL)   │    │  Prometheus     │    │    Grafana      │
│   Port: 80,443  │    │  Port: 9090     │    │   Port: 3000    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │ Musike Backend  │
                    │   Port: 8080    │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   PostgreSQL    │    │     Redis       │    │   Backup        │
│   Port: 5432    │    │   Port: 6379    │    │   Service       │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

## 🚀 Quick Start

### 1. Environment Setup

```bash
# Copy environment template
cp env.example .env

# Edit environment variables
nano .env
```

**Required Environment Variables:**
```bash
# Database
POSTGRES_PASSWORD=your_secure_password_here

# JWT Secret (generate a secure one)
JWT_SECRET=your_very_long_and_secure_jwt_secret_key_here_minimum_256_bits

# Redis
REDIS_PASSWORD=your_secure_redis_password

# CORS (your frontend domains)
CORS_ALLOWED_ORIGINS=https://your-frontend-domain.com,https://www.your-frontend-domain.com

# Optional: Grafana
GRAFANA_PASSWORD=your_grafana_admin_password
```

### 2. SSL Certificate Setup

```bash
# Create SSL directory
mkdir -p nginx/ssl

# Copy your SSL certificates
cp your-cert.pem nginx/ssl/cert.pem
cp your-key.pem nginx/ssl/key.pem
```

### 3. Build and Deploy

```bash
# Development deployment
docker-compose up -d

# Production deployment (with monitoring)
docker-compose -f docker-compose.prod.yml up -d
```

## 🔧 Configuration Files

### Dockerfile
- **Multi-stage build** for optimized image size
- **Security**: Non-root user execution
- **Health checks** for container monitoring
- **JVM optimization** for production workloads

### docker-compose.yml
- **Development setup** with basic services
- **Health checks** for all services
- **Volume persistence** for data
- **Network isolation**

### docker-compose.prod.yml
- **Production-ready** with enhanced security
- **Resource limits** and reservations
- **Load balancing** with multiple replicas
- **Monitoring stack** (Prometheus + Grafana)
- **Automated backups**

## 📊 Monitoring

### Prometheus Metrics
The application exposes metrics at `/actuator/prometheus`:
- JVM metrics (memory, GC, threads)
- HTTP metrics (requests, response times)
- Database connection pool metrics
- Custom business metrics

### Grafana Dashboards
Access Grafana at `http://localhost:3000`:
- **Default credentials**: admin/admin
- **Pre-configured dashboards** for:
  - Application performance
  - Database metrics
  - System resources
  - Custom business metrics

### Health Checks
- **Application**: `http://localhost:8080/actuator/health`
- **Database**: Automatic PostgreSQL health checks
- **Redis**: Automatic Redis health checks

## 🔒 Security Features

### Network Security
- **Isolated networks** for service communication
- **Port binding** to localhost only (production)
- **SSL/TLS termination** at Nginx

### Application Security
- **Non-root user** execution
- **JWT secret** environment variable
- **CORS configuration** for trusted origins
- **Rate limiting** on authentication endpoints

### Database Security
- **Strong authentication** (SCRAM-SHA-256)
- **Encrypted connections**
- **Backup encryption** (optional)

## 💾 Data Persistence

### Volumes
```bash
# Database data
postgres_data:/var/lib/postgresql/data

# Redis data
redis_data:/data

# Application logs
./logs:/app/logs

# Backups
./backups:/backups

# Monitoring data
prometheus_data:/prometheus
grafana_data:/var/lib/grafana
```

### Backup Strategy
```bash
# Manual backup
docker-compose exec backup /backup.sh

# Automated backup (cron job)
0 2 * * * docker-compose exec backup /backup.sh

# List backups
docker-compose exec backup /backup.sh list

# Test backup
docker-compose exec backup /backup.sh test
```

## 🔄 Scaling

### Horizontal Scaling
```bash
# Scale backend instances
docker-compose -f docker-compose.prod.yml up -d --scale musike-backend=3

# Load balancing is handled by Nginx
```

### Vertical Scaling
Adjust resource limits in `docker-compose.prod.yml`:
```yaml
deploy:
  resources:
    limits:
      memory: 4G
      cpus: '2.0'
    reservations:
      memory: 2G
      cpus: '1.0'
```

## 🛠️ Maintenance

### Logs
```bash
# View application logs
docker-compose logs -f musike-backend

# View all logs
docker-compose logs -f

# Export logs
docker-compose logs > logs.txt
```

### Updates
```bash
# Pull latest images
docker-compose pull

# Rebuild application
docker-compose build --no-cache musike-backend

# Restart services
docker-compose up -d
```

### Database Migrations
```bash
# Run migrations (automatic with JPA)
docker-compose restart musike-backend

# Manual migration check
docker-compose exec musike-backend java -jar app.jar --spring.jpa.hibernate.ddl-auto=validate
```

## 🚨 Troubleshooting

### Common Issues

**1. Port Conflicts**
```bash
# Check port usage
netstat -tulpn | grep :8080

# Change ports in docker-compose.yml
ports:
  - "8081:8080"  # Use different host port
```

**2. Memory Issues**
```bash
# Check container memory usage
docker stats

# Increase memory limits
JAVA_OPTS="-Xms2g -Xmx4g"
```

**3. Database Connection Issues**
```bash
# Check database health
docker-compose exec postgres pg_isready -U postgres

# View database logs
docker-compose logs postgres
```

**4. SSL Certificate Issues**
```bash
# Test SSL configuration
openssl s_client -connect localhost:443

# Check Nginx configuration
docker-compose exec nginx nginx -t
```

### Performance Tuning

**JVM Optimization:**
```bash
# Production JVM options
JAVA_OPTS="-Xms2g -Xmx4g -XX:+UseG1GC -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+HeapDumpOnOutOfMemoryError"
```

**Database Optimization:**
```sql
-- PostgreSQL tuning
ALTER SYSTEM SET shared_buffers = '256MB';
ALTER SYSTEM SET effective_cache_size = '1GB';
ALTER SYSTEM SET maintenance_work_mem = '64MB';
```

## 📈 Performance Monitoring

### Key Metrics to Monitor
- **Response Time**: Average API response time
- **Throughput**: Requests per second
- **Error Rate**: Percentage of failed requests
- **Memory Usage**: JVM heap usage
- **Database Connections**: Active connections
- **Cache Hit Rate**: Redis cache efficiency

### Alerts Setup
Configure alerts in Prometheus for:
- High error rates (>5%)
- Slow response times (>2s)
- Memory usage (>80%)
- Database connection issues
- Service unavailability

## 🔄 CI/CD Integration

### GitHub Actions Example
```yaml
name: Deploy to Production
on:
  push:
    branches: [main]

jobs:
  deploy:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Deploy to server
        run: |
          ssh user@server "cd /opt/musike && git pull && docker-compose -f docker-compose.prod.yml up -d --build"
```

## 📚 Additional Resources

- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Prometheus Monitoring](https://prometheus.io/docs/)
- [Grafana Dashboards](https://grafana.com/docs/)

## 🤝 Support

For issues and questions:
1. Check the troubleshooting section
2. Review application logs
3. Check monitoring dashboards
4. Create an issue in the repository

---

**⚠️ Production Checklist:**
- [ ] Environment variables configured
- [ ] SSL certificates installed
- [ ] Database passwords changed
- [ ] JWT secret generated
- [ ] CORS origins configured
- [ ] Monitoring setup verified
- [ ] Backup strategy tested
- [ ] Security headers configured
- [ ] Resource limits set
- [ ] Health checks passing