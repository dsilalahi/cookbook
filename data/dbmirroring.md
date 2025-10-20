| Source | Capabilities | Benefits | Limitations | Implementation Steps |
|--------|--------------|----------|-------------|----------------------|
| Azure SQL Database | - Replicates entire databases and tables into OneLake<br>- Near real-time synchronization using change data capture<br>- Data stored in Delta Lake format for analytics<br>- Supports cross-database queries and joining with other Fabric data<br>- Accessible via SQL analytics endpoint, Power BI, Spark | - Low-cost, low-latency data ingestion without ETL pipelines<br>- Fully managed service, no need to manage replication infrastructure<br>- Free storage up to capacity limit, free background compute for replication<br>- Enables real-time analytics, BI, and AI on operational data<br>- Simplifies data architecture by unifying data in OneLake | - Only writable primary databases supported<br>- Max 500 tables<br>- No support for CDC, Synapse Link, or if already mirrored elsewhere<br>- Precision loss for certain datetime types<br>- No support for clustered columnstore indexes, temporal tables, Always Encrypted, etc.<br>- LOB columns truncated to 1MB<br>- Permissions and masking not propagated<br>- Requires SAMI enabled<br>- No cross-tenant mirroring | 1. Enable System Assigned Managed Identity (SAMI) on Azure SQL logical server.<br>2. Create database principal (login and user) with necessary permissions (SELECT, ALTER ANY EXTERNAL MIRROR).<br>3. In Fabric portal, create Mirrored Azure SQL Database item.<br>4. Connect to Azure SQL Database with server, database, authentication details.<br>5. Choose to mirror all data or select tables.<br>6. Start mirroring and monitor replication status. |
| Azure SQL Managed Instance | - Similar to Azure SQL DB: full database replication to OneLake<br>- Near real-time sync<br>- Delta Lake storage<br>- Integration with Fabric workloads | - Same as general: cost-effective, managed, real-time analytics<br>- Supports high-availability setups (but only primary) | - Only "Always up to date" update policy<br>- No geo-DR support<br>- Max 500 tables<br>- No CDC, transactional replication<br>- Similar table/column restrictions as Azure SQL DB<br>- Requires SAMI, no UAMI<br>- No cross-tenant<br>- Specific region limitations | 1. Enable SAMI on Azure SQL Managed Instance.<br>2. Create database principal with permissions.<br>3. In Fabric, create Mirrored Azure SQL Managed Instance item.<br>4. Connect with server (public endpoint), database, authentication.<br>5. Select mirror all or specific tables.<br>6. Start mirroring and monitor. |
| Snowflake | - Replicates databases/tables to OneLake<br>- Near real-time using streams<br>- Supports any Snowflake version/cloud<br>- Mirror all or select tables | - Brings Snowflake data into Fabric ecosystem without ETL<br>- Low latency, managed<br>- Enables unified analytics across sources | - Max 500 tables<br>- Only native tables (no external, transient, etc.)<br>- Backoff on no updates (up to 1 hour)<br>- Security must be reconfigured in Fabric<br>- Potential slower replication if PREVENT_UNLOAD_TO_INLINE_URL enabled | 1. Ensure Snowflake warehouse, permissions (CREATE STREAM, SELECT, etc.).<br>2. In Fabric, create Mirrored Snowflake database.<br>3. Connect with server, warehouse, authentication (username/password or Entra).<br>4. Select database, mirror all or specific tables.<br>5. Start mirroring and monitor status. |
| Azure Cosmos DB (Preview) | - Mirrors NoSQL databases/containers to OneLake<br>- Near real-time replication using continuous backup<br>- Data as Delta tables, supports nested JSON as strings<br>- Query via SQL analytics endpoint | - Zero-ETL for NoSQL data into analytics platform<br>- Low cost, managed<br>- Enables SQL queries on NoSQL data | - Preview: no production workloads<br>- Only NoSQL API, continuous backup required<br>- No private endpoints, public access only<br>- No support for certain authentications (read-only keys, managed identities)<br>- Delete via TTL not supported<br>- Nested data as JSON strings, size limits (8KB in warehouse)<br>- Schema changes may cause nulls or upcasting | 1. Enable continuous backup and public access on Cosmos DB account.<br>2. In Fabric, create Mirrored Azure Cosmos DB.<br>3. Connect with endpoint, authentication (account key or Entra).<br>4. Select database/containers.<br>5. Start mirroring and monitor.<br>6. Optionally query source via data explorer. |
| Google BigQuery (Preview) | - Mirrors datasets/tables to OneLake<br>- Uses CDC for near real-time (with ~15 min delay)<br>- Supports service account authentication | - Integrates BigQuery data into Fabric without ETL<br>- Managed, low-latency (with noted delay) | - ~10-15 min delay due to BigQuery CDC<br>- Tables without PK: insert-only, non-inserts trigger reseed/backoff<br>- Backoff on no changes (up to 1 hr)<br>- Requires specific permissions for replication | 1. Ensure BigQuery permissions (bigquery.datasets.create, etc.).<br>2. In Fabric, create Mirrored Google BigQuery.<br>3. Connect with service account email and JSON key.<br>4. Select database, mirror all or tables.<br>5. Start mirroring and monitor. |
| Oracle (Preview) | - Mirrors Oracle databases to OneLake using LogMiner<br>- Supports versions 11+<br>- DDL partial support (add/delete/rename columns) | - Brings Oracle data into modern analytics platform<br>- Managed replication | - Max 500 tables, 1 mirror per workspace<br>- Requires archive log, supplemental logging<br>- No Autonomous DB<br>- No tables without PK or long names (>=30 chars)<br>- Limited data types, no column type updates<br>- Requires OPDG | 1. Enable archive log mode on Oracle DB.<br>2. Enable supplemental logging for DB and tables.<br>3. Grant permissions to sync user.<br>4. Install On-Premises Data Gateway.<br>5. In Fabric, create Mirrored Oracle.<br>6. Connect via gateway with server details, auth.<br>7. Select tables (auto or manual).<br>8. Start mirroring. |
| SQL Server (Preview) | - Mirrors on-prem/VM SQL Server databases<br>- Near real-time using change tracking<br>- Supports 2016+ | - Extends on-prem SQL to cloud analytics<br>- Managed, real-time | - No Linux or Azure VM for SQL 2025<br>- Only primary in AG<br>- Max 500 tables<br>- No CDC for 2025<br>- Similar restrictions as Azure SQL: data types, features<br>- Permissions not propagated | 1. Create database principal with permissions.<br>2. For SQL 2025, connect to Azure Arc, enable managed identity via registry/PowerShell.<br>3. In Fabric, create Mirrored SQL Server.<br>4. Connect with server, database, auth.<br>5. Select tables.<br>6. Start mirroring.


Database Mirroring in Microsoft Fabric: Step-by-Step Instructions and Best Practices



Step
Description
Source-Specific Notes
Best Practices



1. Verify Prerequisites
Ensure the source database meets Fabric’s mirroring requirements:- Supported sources: Azure SQL Database, Azure SQL Managed Instance, Snowflake, Azure Cosmos DB (Preview), Google BigQuery (Preview), Oracle (Preview), SQL Server (Preview).- Required permissions for the source (e.g., SELECT, CREATE STREAM for Snowflake, ALTER ANY EXTERNAL MIRROR for Azure SQL).- Enable necessary configurations (e.g., System Assigned Managed Identity (SAMI) for Azure SQL, archive logging for Oracle, continuous backup for Cosmos DB).- Ensure network access (e.g., public endpoint for Cosmos DB, On-Premises Data Gateway for Oracle/SQL Server).
- Azure SQL DB/MI: Enable SAMI on the logical server.- Snowflake: Ensure warehouse and permissions (e.g., CREATE STREAM, SELECT).- Cosmos DB: Enable continuous backup, public access.- BigQuery: Service account with permissions (e.g., bigquery.datasets.create).- Oracle: Enable archive log mode, supplemental logging; install On-Premises Data Gateway.- SQL Server: For 2025, connect to Azure Arc, enable managed identity.
- Verify source database compatibility with Fabric’s limitations (e.g., max 500 tables, no CDC for some sources).- Check region compatibility (e.g., Azure SQL MI has specific region restrictions).- Use a dedicated service account with minimal required permissions to enhance security.


2. Configure Source Database
Set up the source database for mirroring:- Create a database user/principal with required permissions.- Enable necessary features (e.g., change tracking for SQL Server, streams for Snowflake, LogMiner for Oracle).- Ensure the database is accessible from Fabric (e.g., configure firewall rules, public endpoints, or gateways).
- Azure SQL DB/MI: Create login/user with SELECT, ALTER ANY EXTERNAL MIRROR permissions.- Snowflake: Grant CREATE STREAM, SELECT on tables.- Cosmos DB: Ensure account key or Entra authentication.- BigQuery: Provide service account JSON key.- Oracle: Grant LogMiner permissions, configure supplemental logging.- SQL Server: Enable change tracking, grant SELECT permissions.
- Document all permissions and configurations for auditing.- Test connectivity from Fabric to the source before proceeding.- For on-premises sources (Oracle, SQL Server), ensure the gateway is highly available.


3. Access Microsoft Fabric
Log in to the Microsoft Fabric portal (fabric.microsoft.com) with a user account that has workspace admin permissions. Navigate to the desired workspace where the mirror will be created.
- Ensure the workspace has sufficient capacity (e.g., F2 or higher for mirroring).- Verify user has Contributor or Admin role in the workspace.
- Use a dedicated workspace for mirroring to isolate data and manage permissions.- Enable Fabric capacity before starting to avoid interruptions.


4. Create Mirrored Database
In the Fabric portal:1. Click New > Mirrored Database > Select source type (e.g., Azure SQL Database, Snowflake, etc.).2. Enter connection details:   - Server/Endpoint: Source server URL (e.g., server.database.windows.net for Azure SQL).   - Database: Name of the database to mirror.   - Authentication: Provide credentials (e.g., username/password, Entra ID, service account key).3. Choose to mirror all tables or select specific tables (max 500).4. Name the mirrored database in Fabric.
- Azure SQL DB/MI: Use server name, database name, SAMI or SQL authentication.- Snowflake: Specify warehouse, server (e.g., account.snowflakecomputing.com).- Cosmos DB: Provide endpoint, account key, or Entra auth.- BigQuery: Use service account email and JSON key.- Oracle: Connect via On-Premises Data Gateway.- SQL Server: Specify server (on-prem or VM), use Arc for 2025.
- Use descriptive names for mirrored databases (e.g., Mirror_AzureSQL_ProdDB).- Select only necessary tables to optimize performance and reduce capacity usage.- Validate connection details before proceeding to avoid errors.


5. Start Mirroring
Click Create to start the mirroring process. Fabric will:- Validate the connection.- Replicate initial data snapshot to OneLake as Delta tables.- Set up near real-time synchronization (using CDC, streams, or LogMiner, depending on the source).
- Azure SQL DB/MI: Uses change data capture (CDC).- Snowflake: Uses streams for near real-time sync.- Cosmos DB: Leverages continuous backup.- BigQuery: Uses CDC with ~10-15 min delay.- Oracle: Uses LogMiner.- SQL Server: Uses change tracking.
- Monitor initial sync progress in the Fabric UI to ensure completion.- Ensure sufficient storage in OneLake for the initial snapshot.- Avoid schema changes during initial sync to prevent errors.


6. Monitor and Validate
In the Fabric workspace:1. Navigate to the mirrored database under Data Warehouse or Lakehouse.2. Check replication status in the UI (e.g., “Running,” “Paused,” “Error”).3. Query the mirrored data using the SQL analytics endpoint or Spark to verify data accuracy.4. Monitor for errors or delays in the Fabric monitoring hub.
- Cosmos DB: Nested JSON fields are stored as strings; verify schema.- BigQuery: Expect ~10-15 min delay for updates.- Oracle: Ensure LogMiner is running correctly via gateway logs.- SQL Server: Validate change tracking propagation.
- Set up alerts for replication failures in Fabric.- Regularly query a sample of data to ensure consistency with the source.- Check logs for errors (e.g., schema mismatches, connectivity issues).


7. Integrate with Fabric Workloads
Use the mirrored data in Fabric workloads:- Query via SQL analytics endpoint for BI reports.- Use in Spark notebooks for data processing.- Integrate with Power BI for visualizations.- Join with other OneLake data for unified analytics.
- All sources store data as Delta tables in OneLake, enabling cross-source queries.- Cosmos DB data may require parsing JSON strings for nested fields.
- Optimize queries for Delta table performance (e.g., use partitioning).- Secure access to mirrored data with workspace roles (e.g., Viewer, Contributor).- Use semantic models in Power BI for efficient reporting.


8. Manage and Maintain
- Pause/resume mirroring in the Fabric UI if needed (e.g., for maintenance).- Update connection details if credentials change.- Delete the mirrored database if no longer needed, which removes data from OneLake.
- Azure SQL DB/MI: Update SAMI credentials if rotated.- Snowflake/BigQuery: Update service account keys as needed.- Oracle/SQL Server: Ensure gateway remains online.
- Regularly review capacity usage in Fabric to avoid throttling.- Document mirroring configurations for disaster recovery.- Test failover scenarios for critical databases.


**Best Practices for Database Mirroring in Microsoft Fabric

Category
Best Practice


Performance
- Select only necessary tables to reduce replication overhead.- Use high-performance SKUs for source databases (e.g., Business Critical for Azure SQL).- Optimize source database for change tracking (e.g., index primary keys).


Security
- Use least-privilege permissions for the mirroring user.- Enable Entra ID authentication where supported (e.g., Azure SQL, Snowflake).- Restrict network access to the source (e.g., firewall rules, private endpoints where supported).


Schema Management
- Avoid frequent schema changes during mirroring to prevent errors.- For non-additive changes (e.g., column deletion), test impact on downstream processes.- Use Delta tables’ schema evolution to handle additive changes (e.g., new columns).


Monitoring
- Set up monitoring alerts for replication failures or delays.- Regularly check OneLake storage usage for mirrored data.- Validate data consistency between source and mirror periodically.


Cost Optimization
- Leverage Fabric’s free storage and background compute for mirroring.- Monitor capacity usage to avoid unexpected costs.- Pause mirroring during low-usage periods if applicable.


Reliability
- Ensure high availability for on-premises gateways (Oracle, SQL Server).- Test mirroring failover and recovery processes.- Maintain backup configurations in the source database (e.g., continuous backup for Cosmos DB).


Notes

Schema Changes: Most sources handle additive schema changes (e.g., adding columns) automatically. Non-additive changes (e.g., column deletion, type changes) may cause errors or require manual intervention (e.g., updating queries or reconfiguring mirroring).
Limitations: Check source-specific limitations (e.g., max 500 tables, no cross-tenant mirroring, no support for certain features like Always Encrypted in Azure SQL).
Preview Sources: For Cosmos DB, BigQuery, Oracle, and SQL Server (Preview), avoid production workloads until fully supported, and test thoroughly for stability.
