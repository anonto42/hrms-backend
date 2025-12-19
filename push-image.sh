#!/bin/bash
set -e

# Configuration
IMAGE_NAME="hrms-backend"
VERSION="1.0.2"
REGISTRY="docker.io"
USERNAME="anonto"
REPOSITORY="${USERNAME}/${IMAGE_NAME}"

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}Starting Docker image push process...${NC}"

# 1. Build the image
echo -e "${GREEN}Step 1: Building Docker image...${NC}"
docker build -t ${IMAGE_NAME}:latest .

# 2. Tag the image
echo -e "${GREEN}Step 2: Tagging images...${NC}"
docker tag ${IMAGE_NAME}:latest ${REPOSITORY}:latest
docker tag ${IMAGE_NAME}:latest ${REPOSITORY}:${VERSION}

# 3. Login to registry
echo -e "${GREEN}Step 3: Logging in to registry...${NC}"
if [[ "${REGISTRY}" == "ghcr.io" ]]; then
    # For GitHub Container Registry
    echo ${GITHUB_TOKEN} | docker login ghcr.io -u ${USERNAME} --password-stdin
else
    # For Docker Hub
    docker login
fi

# 4. Push images
echo -e "${GREEN}Step 4: Pushing images to registry...${NC}"
docker push ${REPOSITORY}:latest
docker push ${REPOSITORY}:${VERSION}

# 5. Clean up local tags if needed
echo -e "${GREEN}Step 5: Cleaning up local tags...${NC}"
docker rmi ${REPOSITORY}:latest ${REPOSITORY}:${VERSION} || true

echo -e "${GREEN}✅ Successfully pushed images to ${REGISTRY}/${REPOSITORY}${NC}"
echo -e "Tags: latest, ${VERSION}"