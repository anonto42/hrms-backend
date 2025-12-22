#!/bin/bash
set -e

echo "🚀 Starting deployment process..."

# Navigate to deployment directory
cd ~/hrms-deployment

# Pull latest images
echo "📥 Pulling latest Docker images..."
docker compose pull hrms_backend

# Stop only the backend service
echo "⚙  Stopping backend service..."
docker compose stop hrms_backend

# Remove old backend container
echo "🗑  Removing old backend container..."
docker compose rm -f hrms_backend

# Start services
echo "🚀 Starting services with updated image..."
docker compose up -d

# Wait for backend to be healthy
#echo "⏳ Waiting for backend to be healthy..."
#sleep 10

# Health check
#if curl -f http://localhost:8080/actuator/health; then
#    echo "✅ Deployment successful! Backend is healthy."

    # Clean up old images
    echo "🧹 Cleaning up unused Docker images..."
    docker image prune -f

    # Log deployment
    echo "Deployed at $(date)" >> deploy.log
    echo "Image: $(docker inspect --format='{{.Config.Image}}' hrms_backend)" >> deploy.log
#else
#    echo "❌ Health check failed! Rolling back..."

    # Rollback to previous image
 #   docker compose stop hrms_backend
  #  docker compose up -d

   # echo "🔄 Rolled back to previous version"
    #exit 1
#fi
