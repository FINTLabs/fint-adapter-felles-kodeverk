# FINT Adapter Felles Kodeverk

## Configuration properties

| Property | Description | Default |
|----------|-------------|---------|
| `fint.adapter.organizations` | Organizations to deliver data for | _none_ |
| `fint.adapter.sse-endpoint`  | Endpoint for SSE events | _none_ |
| `fint.adapter.status-endpoint` | Endpoint for event status updates | _none_ |
| `fint.adapter.response-endpoint` | Endpoint for event responses | _none_ |
| `fint.adapter.ssb-klass.root-url` | Endpoint for SSB classifications | http://data.ssb.no/api/klass/v1/classifications | 
| `fint.adapter.ssb-klass.kommune` | SSB classification code for _Kommune_ | 131 |
| `fint.adapter.ssb-klass.fylke` | SSB classification code for _Fylke_ | 104 |
| `fint.adapter.ssb-klass.valid-from` | Default _valid from_ date to request from SSB | `1900-01-01` |
| `fint.adapter.ssb-klass.valid-to` | Default _valid to_ date to request from SSB | `2199-12-31` |
| `fint.adapter.ssb-klass.interval` | Default interval to request updates from SSB (cron syntax) | `0 */10 * * * *` |
