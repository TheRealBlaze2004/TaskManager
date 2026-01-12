# TaskManager
The core authentication flow (JWT-based login, role-based access, profile viewing)
is fully implemented and working.

Task APIs are implemented with Redis-based caching. While the application runs
successfully in Docker, there is a known Redis startup timing issue that can
cause task endpoints to fail if Redis is not fully ready when the backend starts.

This issue is documented in logs and is reproducible. A fix using Redis health
checks and conditional cache initialization is planned.
Despite this, the system architecture, security, and frontend integration are complete.
