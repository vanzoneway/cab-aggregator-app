services=(
"passenger-service"
"rating-service"
"rides-service"
"discovery-service"
"driver-service"
"gateway-service")
for service in "${services[@]}"; do
    echo "Building $service..."
    docker build -f Dockerfile --build-arg SERVICE_NAME="$service" -t modsen/"$service":v1.0 ../"$service"
    if [ $? -ne 0 ]; then
        echo "Failed to build $service. Exiting."
        exit 1
    fi
done

echo "All services built successfully!"