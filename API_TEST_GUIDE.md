# API Testing Guide

## Quick Test Commands

### 1. Test Admin Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@voting.com",
    "password": "admin123"
  }'
```

Expected Response:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "admin@voting.com",
  "name": "System Administrator",
  "role": "ADMIN"
}
```

### 2. Register a Voter
```bash
curl -X POST http://localhost:8080/api/auth/register/voter \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "city": "New York"
  }'
```

### 3. Create a Candidate (Admin only)
```bash
curl -X POST http://localhost:8080/api/admin/candidates \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "name": "Jane Smith",
    "party": "Independent",
    "description": "Experienced leader for change",
    "electionId": 1
  }'
```

### 4. Assign Voters by City (Admin only)
```bash
curl -X POST http://localhost:8080/api/admin/assign-voters \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN" \
  -d '{
    "city": "New York"
  }'
```

### 5. Voter Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john.doe@example.com",
    "password": "password123"
  }'
```

### 6. Get Candidates for Election (Voter)
```bash
curl -X GET http://localhost:8080/api/voter/candidates/1 \
  -H "Authorization: Bearer YOUR_VOTER_TOKEN"
```

### 7. Cast a Vote (Voter)
```bash
curl -X POST http://localhost:8080/api/voter/vote \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_VOTER_TOKEN" \
  -d '{
    "candidateId": 1,
    "electionId": 1
  }'
```

### 8. Get Election Results (Admin)
```bash
curl -X GET http://localhost:8080/api/admin/results/1 \
  -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
```

## Application URLs

- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:votingdb`
  - Username: `sa`
  - Password: `password`

## Default Credentials

**Admin Account:**
- Email: admin@voting.com
- Password: admin123

## Testing Workflow

1. Start the application: `mvn spring-boot:run`
2. Login as admin to get JWT token
3. Register some voters
4. Create candidates for the election
5. Assign voters by city
6. Login as voter to get voter JWT token
7. View candidates and cast votes
8. Check election results as admin

## Important Notes

- Replace `YOUR_ADMIN_TOKEN` and `YOUR_VOTER_TOKEN` with actual JWT tokens from login responses
- The application creates a default election on startup
- Voting is only allowed within the election time window
- Each voter can only vote once per election
- Voters must be assigned before they can vote
