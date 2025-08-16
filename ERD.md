# Entity Relationship Diagram (ERD)

## Overview
This document describes the database schema for the Online Voting System.

## Entities and Relationships

### 1. User (Abstract Entity)
- **Table**: `users`
- **Type**: Single Table Inheritance
- **Discriminator Column**: `user_type`

#### Attributes:
- `id` (Primary Key, Auto-generated)
- `email` (Unique, Not Null)
- `password` (Not Null, Encrypted)
- `name` (Not Null)
- `role` (Enum: ADMIN, VOTER)
- `created_at` (Timestamp)
- `user_type` (Discriminator: ADMIN/VOTER)

### 2. Admin (Extends User)
- **Discriminator Value**: "ADMIN"
- **Role**: ADMIN
- No additional attributes

### 3. Voter (Extends User)
- **Discriminator Value**: "VOTER"
- **Role**: VOTER

#### Additional Attributes:
- `city` (Not Null)
- `is_assigned` (Boolean, default: false)

#### Relationships:
- **One-to-Many** with Vote (`votes`)

### 4. Election
- **Table**: `elections`

#### Attributes:
- `id` (Primary Key, Auto-generated)
- `title` (Not Null)
- `description` (Not Null)
- `start_time` (Not Null)
- `end_time` (Not Null)
- `created_at` (Timestamp)

#### Relationships:
- **One-to-Many** with Candidate (`candidates`)
- **One-to-Many** with Vote (`votes`)

### 5. Candidate
- **Table**: `candidates`

#### Attributes:
- `id` (Primary Key, Auto-generated)
- `name` (Not Null)
- `party` (Not Null)
- `description` (Optional)
- `election_id` (Foreign Key, Not Null)
- `created_at` (Timestamp)

#### Relationships:
- **Many-to-One** with Election (`election`)
- **One-to-Many** with Vote (`votes`)

### 6. Vote
- **Table**: `votes`
- **Unique Constraint**: (voter_id, election_id) - Ensures one vote per voter per election

#### Attributes:
- `id` (Primary Key, Auto-generated)
- `voter_id` (Foreign Key, Not Null)
- `candidate_id` (Foreign Key, Not Null)
- `election_id` (Foreign Key, Not Null)
- `voted_at` (Timestamp)

#### Relationships:
- **Many-to-One** with Voter (`voter`)
- **Many-to-One** with Candidate (`candidate`)
- **Many-to-One** with Election (`election`)

## ERD Diagram (Text Representation)

```
┌─────────────────┐
│      User       │
│   (Abstract)    │
├─────────────────┤
│ +id (PK)        │
│ +email (UQ)     │
│ +password       │
│ +name           │
│ +role           │
│ +created_at     │
│ +user_type      │
└─────────────────┘
         △
         │ (Inheritance)
    ┌────┴────┐
    │         │
┌───▽───┐ ┌──▽────┐
│ Admin │ │ Voter │
├───────┤ ├───────┤
│       │ │ +city │
│       │ │+is_assigned
└───────┘ └───┬───┘
              │
              │ 1:N
              ▽
          ┌───────┐
          │ Vote  │
          ├───────┤
          │+id(PK)│◄────┐
          │+voter_id(FK) │
          │+candidate_id(FK)
          │+election_id(FK)
          │+voted_at│
          └───┬───┘
              │ N:1
              ▽
      ┌───────────┐         ┌─────────────┐
      │Candidate  │         │  Election   │
      ├───────────┤         ├─────────────┤
      │+id (PK)   │         │+id (PK)     │
      │+name      │         │+title       │
      │+party     │         │+description │
      │+description│         │+start_time  │
      │+election_id(FK)───────►+end_time   │
      │+created_at│         │+created_at  │
      └───────────┘         └─────────────┘
           △                        │
           │ 1:N                    │ 1:N
           └────────────────────────┘
```

## Constraints and Business Rules

### 1. Unique Constraints
- `users.email` - Each email must be unique across all users
- `votes(voter_id, election_id)` - One vote per voter per election

### 2. Foreign Key Constraints
- `candidates.election_id` → `elections.id`
- `votes.voter_id` → `users.id` (where user_type = 'VOTER')
- `votes.candidate_id` → `candidates.id`
- `votes.election_id` → `elections.id`

### 3. Business Rules
- Voters must be assigned (`is_assigned = true`) to participate in voting
- Voting is only allowed within the election time window (`start_time` to `end_time`)
- Each voter can vote only once per election
- Candidates must belong to a specific election
- Admin users cannot vote, Voter users cannot perform admin operations

### 4. Validation Rules
- Email must be in valid email format
- Password must be at least 6 characters
- Names must be between 2-100 characters
- City must be between 2-50 characters
- Party name must be between 2-50 characters
- Description can be up to 500 characters

## Database Indexes (Recommended)

```sql
-- Unique indexes (automatically created)
CREATE UNIQUE INDEX idx_users_email ON users(email);
CREATE UNIQUE INDEX idx_votes_voter_election ON votes(voter_id, election_id);

-- Performance indexes
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_user_type ON users(user_type);
CREATE INDEX idx_voters_city ON users(city) WHERE user_type = 'VOTER';
CREATE INDEX idx_voters_assigned ON users(is_assigned) WHERE user_type = 'VOTER';
CREATE INDEX idx_candidates_election ON candidates(election_id);
CREATE INDEX idx_votes_election ON votes(election_id);
CREATE INDEX idx_votes_candidate ON votes(candidate_id);
CREATE INDEX idx_elections_time ON elections(start_time, end_time);
```

## Sample Data Flow

1. **Admin Registration**: Admin is created with role ADMIN
2. **Election Creation**: Admin creates an election with start/end times
3. **Candidate Registration**: Admin registers candidates for the election
4. **Voter Registration**: Voters register with their city information
5. **Voter Assignment**: Admin assigns voters by city (sets `is_assigned = true`)
6. **Voting Process**: Assigned voters can vote within the election time window
7. **Results Calculation**: System aggregates votes by candidate for each election

This ERD ensures data integrity, prevents duplicate voting, and supports the complete voting workflow as required by the assignment.
