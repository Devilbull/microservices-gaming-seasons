Write-Host "🚀 Starting DEV environment..." -ForegroundColor Green

# 1️⃣ Docker infra
Write-Host "🐳 Starting Docker services..."
Start-Process powershell -ArgumentList `
  "-NoExit", `
  "-Command cd docker; docker compose up -d" `
  -WindowStyle Normal

Start-Sleep -Seconds 5

# 2️⃣ Spring Boot services
$services = @(
  "userservice"
#,
#  "notification-service",
#  "analytics-service",
#  "gateway-service"
)

foreach ($service in $services) {
    Write-Host "☕ Starting $service..."
    Start-Process powershell -ArgumentList `
      "-NoExit", `
      "-Command cd $service; mvn spring-boot:run" `
      -WindowStyle Normal
}

# 3️⃣ Frontend
Write-Host "⚛️ Starting frontend..."
Start-Process powershell -ArgumentList `
  "-NoExit", `
  "-Command cd frontend; npm run dev" `
  -WindowStyle Normal
