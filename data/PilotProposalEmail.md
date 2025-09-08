Subject: Proposal: Pilot Project for Incremental Data Ingestion from Infor LX to Microsoft Fabric

Dear [Recipient/Team Name],

I hope this message finds you well. I propose a 3-week pilot project to test incremental batch ingestion from Infor LX (DB2) to Microsoft Fabric using Fabric Data Factory, replacing Qlik Replicate with a Microsoft-native solution. This pilot will leverage a synthetic watermark based on an existing unique, monotonically increasing column (e.g., `ORDER_ID`) to track changes, as the source tables lack a timestamp column, ensuring efficient and accurate data ingestion for analytics.

### Pilot Overview
**Objective**: Validate reliable incremental data ingestion from a selected Infor LX table to Fabric’s Lakehouse, using a synthetic watermark (`ORDER_ID`) to capture new rows, supporting scalability and analytics readiness.

**Scope**:
- **Source**: One or two Infor LX tables (e.g., orders or inventory, <1M rows) with a unique, increasing ID column (e.g., `ORDER_ID`).
- **Tool**: Fabric Data Factory with an on-premises data gateway for secure DB2 connectivity.
- **Ingestion Pattern**: Incremental batch (hourly updates), tracking new rows via `ORDER_ID` as a synthetic watermark.
- **Duration**: 3 weeks (planning, setup, testing, and review).
- **Team**: Data engineer, Infor LX DBA, and business analyst (with stakeholder input).

### Synthetic Watermark Strategy
Since the source tables lack a timestamp column, we will use an existing unique, monotonically increasing column (e.g., `ORDER_ID`) as a synthetic watermark to identify new rows. A control table in Fabric will store the last processed `ORDER_ID`, enabling the pipeline to query only rows with `ORDER_ID` greater than the stored value. This approach avoids source schema changes and captures new data efficiently, though updates/deletes may require additional logic in a future phase.

### Key Steps
1. **Setup (Week 1)**: Configure Fabric workspace, install on-premises data gateway, and create a control table to store the last processed `ORDER_ID` (e.g., via Spark: `CREATE TABLE watermark_table (table_name STRING, last_id BIGINT)`).
2. **Pipeline Development (Week 1-2)**: Build a Data Factory pipeline with:
   - **Lookup Activity**: Retrieve the last `ORDER_ID` from the control table.
   - **Copy Activity**: Ingest new rows (e.g., `SELECT * FROM your_table WHERE ORDER_ID > @{activity('Lookup').output.firstRow.last_id}`).
   - **Watermark Update**: Store the new maximum `ORDER_ID` post-copy.
3. **Testing and Validation (Week 2-3)**: Perform initial full load, simulate new rows in Infor LX, and validate data accuracy in Fabric. Monitor performance and error handling.
4. **Review (Week 3)**: Demo ingested data in Power BI and gather stakeholder feedback.

### Success Criteria
- **Accuracy**: 100% capture of new rows (`ORDER_ID > last_id`), verified by row counts and checksums.
- **Performance**: Pipeline completes in <30 minutes for ~10K rows; latency <1 hour.
- **Reliability**: 95%+ success rate over 20 runs, with robust error handling.
- **Usability**: Stakeholders confirm data supports reporting needs (e.g., via Power BI).
- **Scalability**: Pipeline adaptable to additional tables with minimal changes.

### Benefits
- **Microsoft-Native**: Leverages Fabric Data Factory, reducing dependency on third-party tools like Qlik Replicate.
- **Cost Efficiency**: Utilizes existing Fabric capacity; no additional licensing required.
- **Strategic Alignment**: Enhances analytics capabilities and supports data modernization.
- **Stakeholder Trust**: Builds confidence through a low-risk pilot, demonstrating Microsoft solutions.

### Next Steps
- **Approval**: Please confirm support by [date, e.g., September 12, 2025].
- **Kickoff Meeting**: Schedule a 30-minute session to finalize table selection and resources.
- **Resource Allocation**: Secure access to Infor LX test environment and Fabric workspace.

This pilot aligns with our role in guiding technical decisions, fostering stakeholder relationships, and ensuring deployment readiness. It will provide insights for a full rollout, with potential to explore update/delete handling if needed.

Please share your feedback or any adjustments you’d like to discuss. I’m available to walk through the approach or demo Fabric’s capabilities.

Best regards,  
[Your Name]  
[Your Job Title]  
[Your Contact Information]