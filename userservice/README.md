# API Dokumentacija

---

## **Authentication**

| Endpoint / Event           | Method | Access        | Description                                      |
| -------------------------- | ------ | ------------- | ------------------------------------------------ |
| `/auth/register`           | POST   | Public        | Registracija igrača (activated=false).           |
| `/auth/activate?token=XYZ` | GET    | Public        | Aktivacija naloga preko email linka.             |
| `/auth/login`              | POST   | Public        | Login — generiše JWT i šalje u HTTP-only cookie. |
| `/auth/logout`             | POST   | Authenticated | Briše JWT cookie (logout).                       |

---

## **User Profile**

| Endpoint / Event | Method | Access        | Description                        |
| ---------------- | ------ | ------------- | ---------------------------------- |
| `/users/me`      | GET    | Authenticated | Vraća profil ulogovanog korisnika. |
| `/users/me`      | PUT    | Authenticated | Menja osnovne podatke korisnika.   |

---

## **Admin**

| Endpoint / Event            | Method | Access | Description                                          |
| --------------------------- | ------ | ------ | ---------------------------------------------------- |
| `/admin/users`              | GET    | Admin  | Lista svih korisnika.                                |
| `/admin/users/{id}/block`   | PUT    | Admin  | Blokira korisnika — sprečava login i rad aplikacije. |
| `/admin/users/{id}/unblock` | PUT    | Admin  | Odblokira korisnika.                                 |

---

## **Internal (Gaming → User)**

| Endpoint / Event                    | Method | Access                        | Description                            |
| ----------------------------------- | ------ | ----------------------------- | -------------------------------------- |
| `/internal/users/{id}/stats/update` | POST   | Internal (service-to-service) | Gaming servis šalje update statistike. |

---

## **Internal (User → Notification)**

| Event                 | Method | Access   | Description                                      |
| --------------------- | ------ | -------- | ------------------------------------------------ |
| Send activation email | N/A    | Internal | Nakon registracije, šalje event Notif servisu.   |
| Title changed         | N/A    | Internal | Kada se titula promeni, obaveštava Notif servis. |

---

## **Automatska pravila**

| Event                    | Method | Access   | Description                                           |
| ------------------------ | ------ | -------- | ----------------------------------------------------- |
| Recalculate title        | N/A    | Internal | Računa titulu na osnovu uspešno organizovanih sesija. |
| Prevent login if blocked | N/A    | Internal | Blokiran igrač ne može da se loginuje.                |
