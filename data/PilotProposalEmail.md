Source,Capabilities,Benefits,Limitations,Implementation Steps
Azure SQL Database,"- Replicates entire databases and tables into OneLake
- Near real-time synchronization using change data capture
- Data stored in Delta Lake format for analytics
- Supports cross-database queries and joining with other Fabric data
- Accessible via SQL analytics endpoint, Power BI, Spark","- Low-cost, low-latency data ingestion without ETL pipelines
- Fully managed service, no need to manage replication infrastructure
- Free storage up to capacity limit, free background compute for replication
- Enables real-time analytics, BI, and AI on operational data
- Simplifies data architecture by unifying data in OneLake","- Only writable primary databases supported
- Max 500 tables
- No support for CDC, Synapse Link, or if already mirrored elsewhere
- Precision loss for certain datetime types
- No support for clustered columnstore indexes, temporal tables, Always Encrypted, etc.
- LOB columns truncated to 1MB
- Permissions and masking not propagated
- Requires SAMI enabled
- No cross-tenant mirroring","1. Enable System Assigned Managed Identity (SAMI) on Azure SQL logical server.
2. Create database principal (login and user) with necessary permissions (SELECT, ALTER ANY EXTERNAL MIRROR).
3. In Fabric portal, create Mirrored Azure SQL Database item.
4. Connect to Azure SQL Database with server, database, authentication details.
5. Choose to mirror all data or select tables.
6. Start mirroring and monitor replication status."
Azure SQL Managed Instance,"- Similar to Azure SQL DB: full database replication to OneLake
- Near real-time sync
- Delta Lake storage
- Integration with Fabric workloads","- Same as general: cost-effective, managed, real-time analytics
- Supports high-availability setups (but only primary)","- Only ""Always up to date"" update policy
- No geo-DR support
- Max 500 tables
- No CDC, transactional replication
- Similar table/column restrictions as Azure SQL DB
- Requires SAMI, no UAMI
- No cross-tenant
- Specific region limitations","1. Enable SAMI on Azure SQL Managed Instance.
2. Create database principal with permissions.
3. In Fabric, create Mirrored Azure SQL Managed Instance item.
4. Connect with server (public endpoint), database, authentication.
5. Select mirror all or specific tables.
6. Start mirroring and monitor."
Snowflake,"- Replicates databases/tables to OneLake
- Near real-time using streams
- Supports any Snowflake version/cloud
- Mirror all or select tables","- Brings Snowflake data into Fabric ecosystem without ETL
- Low latency, managed
- Enables unified analytics across sources","- Max 500 tables
- Only native tables (no external, transient, etc.)
- Backoff on no updates (up to 1 hour)
- Security must be reconfigured in Fabric
- Potential slower replication if PREVENT_UNLOAD_TO_INLINE_URL enabled","1. Ensure Snowflake warehouse, permissions (CREATE STREAM, SELECT, etc.).
2. In Fabric, create Mirrored Snowflake database.
3. Connect with server, warehouse, authentication (username/password or Entra).
4. Select database, mirror all or specific tables.
5. Start mirroring and monitor status."
Azure Cosmos DB (Preview),"- Mirrors NoSQL databases/containers to OneLake
- Near real-time replication using continuous backup
- Data as Delta tables, supports nested JSON as strings
- Query via SQL analytics endpoint","- Zero-ETL for NoSQL data into analytics platform
- Low cost, managed
- Enables SQL queries on NoSQL data","- Preview: no production workloads
- Only NoSQL API, continuous backup required
- No private endpoints, public access only
- No support for certain authentications (read-only keys, managed identities)
- Delete via TTL not supported
- Nested data as JSON strings, size limits (8KB in warehouse)
- Schema changes may cause nulls or upcasting","1. Enable continuous backup and public access on Cosmos DB account.
2. In Fabric, create Mirrored Azure Cosmos DB.
3. Connect with endpoint, authentication (account key or Entra).
4. Select database/containers.
5. Start mirroring and monitor.
6. Optionally query source via data explorer."
Google BigQuery (Preview),"- Mirrors datasets/tables to OneLake
- Uses CDC for near real-time (with ~15 min delay)
- Supports service account authentication","- Integrates BigQuery data into Fabric without ETL
- Managed, low-latency (with noted delay)","- ~10-15 min delay due to BigQuery CDC
- Tables without PK: insert-only, non-inserts trigger reseed/backoff
- Backoff on no changes (up to 1 hr)
- Requires specific permissions for replication","1. Ensure BigQuery permissions (bigquery.datasets.create, etc.).
2. In Fabric, create Mirrored Google BigQuery.
3. Connect with service account email and JSON key.
4. Select database, mirror all or tables.
5. Start mirroring and monitor."
Oracle (Preview),"- Mirrors Oracle databases to OneLake using LogMiner
- Supports versions 11+
- DDL partial support (add/delete/rename columns)","- Brings Oracle data into modern analytics platform
- Managed replication","- Max 500 tables, 1 mirror per workspace
- Requires archive log, supplemental logging
- No Autonomous DB
- No tables without PK or long names (>=30 chars)
- Limited data types, no column type updates
- Requires OPDG","1. Enable archive log mode on Oracle DB.
2. Enable supplemental logging for DB and tables.
3. Grant permissions to sync user.
4. Install On-Premises Data Gateway.
5. In Fabric, create Mirrored Oracle.
6. Connect via gateway with server details, auth.
7. Select tables (auto or manual).
8. Start mirroring."
SQL Server (Preview),"- Mirrors on-prem/VM SQL Server databases
- Near real-time using change tracking
- Supports 2016+","- Extends on-prem SQL to cloud analytics
- Managed, real-time","- No Linux or Azure VM for SQL 2025
- Only primary in AG
- Max 500 tables
- No CDC for 2025
- Similar restrictions as Azure SQL: data types, features
- Permissions not propagated","1. Create database principal with permissions.
2. For SQL 2025, connect to Azure Arc, enable managed identity via registry/PowerShell.
3. In Fabric, create Mirrored SQL Server.
4. Connect with server, database, auth.
5. Select tables.
6. Start mirroring."
