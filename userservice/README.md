# User Service API

This service is part of a microservices system and provides functionalities related to users: registration, login, profile management, administration, and internal service-to-service calls.

---

## **Authentication**

| Endpoint / Event           | Method | Access        | Description                                      |
| -------------------------- | ------ | ------------- | ------------------------------------------------ |
| `/auth/register`           | POST   | Public        | User registration (activated=false).           |
| `/auth/activate?token=XYZ` | GET    | Public        | Account activation via email link.             |
| `/auth/login`              | POST   | Public        | Login — generates JWT and sends it in HTTP-only cookie. |
| `/auth/logout`             | POST   | Authenticated | Deletes JWT cookie (logout).                   |
| `/auth/passwordForget`     | POST   | Public        | Generates a password reset token and sends email. |
| `/auth/passwordReset`      | POST   | Public        | Resets password using the token from email.   |

---

## **User Profile**

| Endpoint / Event        | Method | Access        | Description                                   |
| ----------------------- | ------ | ------------- | --------------------------------------------- |
| `/users/me`             | GET    | Authenticated | Returns the logged-in user's profile.        |
| `/users/me`             | PUT    | Authenticated | Updates the logged-in user's profile.        |
| `/users/me/password`    | PATCH  | Authenticated | Changes the password and clears the JWT cookie. |

---

## **Admin**

| Endpoint / Event            | Method | Access | Description                                          |
| --------------------------- | ------ | ------ | ---------------------------------------------------- |
| `/admin/users`              | GET    | Admin  | Returns a list of all users.                        |
| `/admin/users/{id}/block`   | PUT    | Admin  | Blocks a user — prevents login and access.          |
| `/admin/users/{id}/unblock` | PUT    | Admin  | Unblocks a user.                                    |

---

## **Internal (Service-to-Service)**

| Endpoint / Event                     | Method | Access                        | Description                                     |
| ------------------------------------ | ------ | ----------------------------- | ----------------------------------------------- |
| `/internal/users/{id}/stats/update`  | POST   | Internal (service-to-service) | Gaming service sends user stats updates.       |
| `/internal/users/{id}/title/recalc`  | POST   | Internal                      | Recalculates user's title based on system rules. |

---

## **Internal (User → NotificationService)**

| Event                     | Method | Access   | Description                                      |
| ------------------------- | ------ | -------- | ------------------------------------------------ |
| Send activation email     | N/A    | Internal | Sends activation email after user registration. |
| Notify title changed      | N/A    | Internal | Notifies NotificationService when user's title changes. |

---

## **JWT Authentication**

- JWT is sent in an **HTTP-only cookie** named `jwt`.
- Login endpoint generates the JWT.
- Logout endpoint deletes the cookie.
- All protected endpoints (`/users/me`, `/admin/...`) require a valid JWT.

---

## **Conventions**

- Frontend-facing endpoints are prefixed with `/auth`, `/users`, `/admin`.
- Internal service-to-service endpoints are prefixed with `/internal`.
- Services communicate directly using **Eureka service discovery**, not via API Gateway (unless otherwise specified).

---

## **Guidelines for Developers**

- All endpoints return JSON.
- Errors are standardized with `error` and `message` fields.
- Admin and internal endpoints require proper roles (e.g., `ROLE_ADMIN`).

---

## **Example Login Request**

```http
POST /auth/login
Content-Type: application/json
200 OK
{
  "meesage": "loged in",
}
