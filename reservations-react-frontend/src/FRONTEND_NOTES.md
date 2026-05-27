# Updated Reservation Frontend

Set the backend URL in `.env` if needed:

```env
VITE_API_BASE_URL=http://localhost:8085/api
```

Main integrations included:

- Public catalogue: `/api/shows` with search, location filter, bookable filter, sorting and pagination.
- Show details: `/api/shows/{id}`, `/api/representations`, `/api/representations/{id}/availability`, `/api/prices`, `/api/reviews`.
- Reservations: `/api/reservations`, `/api/reservations/me`, `/api/reservations/{id}/cancel`.
- Authentication: `/api/auth/signup`, `/api/auth/login`, `/api/auth/me`.
- Member profile: `/api/users/{id}`.
- Admin back-office CRUD for users, roles, shows, locations, localities, prices, artists, artist types, assignments, collaborations, representations, reviews, reservations, affiliate plans and API keys.
- Tools: external import, CSV import/export and RSS feed link.
- Affiliate API page using `X-API-KEY` against `/api/affiliate/shows`.
