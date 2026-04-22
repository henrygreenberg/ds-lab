#!/bin/bash

echo "💣 Resetting environment (THIS WILL DELETE DATA)..."

docker compose down -v

echo "🚀 Starting fresh..."

docker compose up -d

echo "✅ Fresh start complete"
