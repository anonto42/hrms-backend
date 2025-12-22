# backup.sh
#!/bin/bash
BACKUP_DIR="$HOME/hrms-deployment/backups/$(date +%Y%m%d_%H%M%S)"
mkdir -p $BACKUP_DIR

echo "📦 Starting backup..."

# Backup PostgreSQL
docker exec hrms_postgres pg_dump -U hrms_user hrmf > $BACKUP_DIR/postgres_backup.sql

# Backup MongoDB
docker exec hrms_mongo mongodump --username hrms_mongo_user --password your_password --authenticationDatabase admin --out $BACKUP_DIR/mongo_backup

# Backup Docker volumes
cp -r ~/hrms-deployment/data $BACKUP_DIR/

# Compress backup
tar -czf $BACKUP_DIR.tar.gz $BACKUP_DIR

echo "✅ Backup completed: $BACKUP_DIR.tar.gz"

# Keep only last 7 days of backups
find ~/hrms-deployment/backups -name "*.tar.gz" -mtime +7 -delete