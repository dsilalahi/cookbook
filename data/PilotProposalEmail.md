Database Type,Capabilities,Benefits,Limitations,Step-by-Step Implementation
Azure SQL Database,"- Near real-time replication of databases and tables into OneLake in Delta Lake format.
- Supports cross-database queries using T-SQL.
- Analytics-ready for data engineering, science, and Power BI visualization.
- Mirror all data or select specific tables.
- Automatic synchronization of metadata and data changes.","- Low-cost and low-latency data integration without ETL pipelines.
- Enables up-to-date analytics and breaks data silos.
- Free storage up to capacity-based limit (e.g., 1 TB per unit).
- Free background compute for replication.
- Supports secure sharing with RLS and OLS.
- Integrates with Direct Lake for high-performance querying.
- Customizable data retention (default 1-7 days).","- Supported only on writable primary databases.
- Cannot mirror if CDC, Azure Synapse Link, or already mirrored elsewhere.
- Max 500 tables.
- Requires specific permissions (ALTER ANY EXTERNAL MIRROR, etc.).
- No propagation of row/object permissions, dynamic masking, sensitivity labels.
- SAMI must be enabled and primary.
- No cross-tenant mirroring.
- Unsupported primary key types: sql_variant, timestamp.
- Precision loss for datetime2(7), etc.
- No clustered columnstore indexes.
- LOB >1MB truncated.
- Unsupported features: temporal/ledger history, Always Encrypted, in-memory, graph, external tables.
- No DDL ops like switch partition, alter PK.
- DDL changes restart full snapshot.
- No json/vector types.
- No computed columns.
- Unsupported data types: image, text/ntext, xml, rowversion, sql_variant.
- Delayed durability not supported.
- .dacpac needs /p:DoNotAlterReplicatedObjects=False.","1. Enable System Assigned Managed Identity (SAMI) on Azure SQL Logical Server via Azure portal (Security > Identity > On).
2. Verify SAMI with T-SQL: SELECT * FROM sys.dm_server_managed_identities.
3. Create database principal: Connect to master DB, create login (SQL/Entra/SPN/Workspace), add to ##MS_ServerStateReader## role.
4. Connect to user DB, create user, grant SELECT and ALTER ANY EXTERNAL MIRROR.
5. In Fabric portal, create Mirrored Azure SQL Database in workspace.
6. Connect to Azure SQL DB: Provide server, database, auth details (Basic/Org/Service Principal/Workspace).
7. Configure mirroring: Mirror all or select tables, start mirroring.
8. Monitor replication status until Running."
SQL Server,"- Near real-time replication into OneLake in Delta Lake format.
- Supports cross-database queries.
- Analytics-ready for various workloads.
- Mirror all or specific tables.
- Synchronization of changes.","- Same as above: Low-cost, no ETL, free storage/compute up to limits, secure sharing, Direct Lake integration, customizable retention.","- Preview feature.
- Supported only on primary DB of availability group.
- Not for SQL Server 2025 on Azure VM or Linux.
- Cannot mirror if Azure Synapse Link or already mirrored.
- No CDC for SQL 2025.
- Max 500 tables.
- Permissions not propagated (row/object, masking, labels).
- Principal needs ALTER ANY EXTERNAL MIRROR.
- No cross-tenant.
- Unsupported PK types: sql_variant, timestamp.
- For 2016-2022: Requires PK.
- Precision loss for datetime2(7), etc.
- No clustered columnstore.
- LOB >1MB truncated.
- Unsupported: temporal/ledger, Always Encrypted, in-memory, graph, external tables.
- No DDL: switch partition, alter PK.
- DDL changes restart snapshot.
- No json/vector.
- Unsupported data types: CLR, vector, json, geometry, geography, hierarchyid, sql_variant, timestamp, xml, UDT, image, text/ntext.
- No computed columns.
- Columns with spaces/special chars supported but may need quoting.
- Delayed durability not supported.
- .dacpac needs specific property.","1. Ensure prerequisites: SQL instance meets requirements, create principal.
2. Create login in master DB (SQL/Entra), add to ##MS_ServerStateReader##.
3. For Always On: Create login on all instances with same SID.
4. In user DB: Create user, grant SELECT and ALTER ANY EXTERNAL MIRROR.
5. For SQL 2025: Connect server to Azure Arc, enable managed identity via PowerShell script to add registry keys.
6. In Fabric portal, create Mirrored SQL Server Database.
7. Connect: Provide server, database, auth details.
8. Configure: Mirror all or select tables, start.
9. Monitor until Running."
Azure Cosmos DB,"- Near real-time replication from NoSQL accounts into OneLake Delta tables.
- Supports querying source via data explorer (read-only).
- Analytics via SQL endpoint with aggregates and joins.
- Mirror entire DB or specific containers.
- Integrates with Fabric workloads.","- Same general benefits.
- No impact on source performance.
- Enables analytical queries without RUs on mirrored data.
- Simplifies integration with Fabric ecosystem.","- Preview feature.
- Requires continuous backup (7/30 days).
- Public network access only.
- Mirror one DB at a time.
- Can mirror same DB multiple times in workspace.
- Requires specific RBAC for Entra auth.
- Reads on source consume RUs.
- Account/database limitations per continuous backup docs.","1. In Azure portal: Enable continuous backup, set public access.
2. In Fabric: Create Mirrored Azure Cosmos DB (Preview) in workspace.
3. Connect: Provide endpoint, auth (Account key/Org), select DB/containers.
4. Start mirroring.
5. Monitor replication.
6. Query source via View > Source database.
7. Analyze mirrored via SQL analytics endpoint, run queries/joins."
Snowflake,"- Near real-time replication into OneLake Delta tables.
- Supports any Snowflake version/cloud.
- Mirror all data (including future tables) or specific tables.
- Secure connections via gateways for private networks.","- Same general benefits.
- Enables Snowflake data in Fabric analytics.
- Real-time sync with monitoring.","- Max 500 tables.
- Granular security must be re-configured in Fabric.
- Replicator backs off up to 1 hour if no updates.
- Requires specific permissions: CREATE STREAM, SELECT/SHOW/DESCRIBE tables.
- Needs gateway for private networks.","1. Ensure prerequisites: Snowflake warehouse, Fabric capacity, user permissions, networking.
2. In Fabric: Create Mirrored Snowflake in workspace.
3. Connect: Provide server, warehouse, auth (username/password), gateway if needed, select DB.
4. Configure: Mirror all or select tables.
5. Start mirroring.
6. Monitor replication status."
