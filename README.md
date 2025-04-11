# AttachmentRequest Plugin

This plugin allows you to certify a user by providing attachments.

## XPage: `AttachmentRequestXPage`

- **Entry URL**:  
  `jsp/site/Portal.jsp?page=attachmentrequest&view=formAttachmentRequest`

- **Required URL Parameter**:  
  `appCode`

- **Example URL**:  
  `jsp/site/Portal.jsp?page=attachmentrequest&view=formAttachmentRequest&appCode=<REPLACE BY APPCODE>`

---

## Configuration

### 1. S3 Server

In order to upload attachments to an S3 server, it is necessary to configure properties.

Override the `sthree.properties` file with the following properties:

```properties
s3Url=<Replace with your server URL>
s3Key=<Replace with your S3 key>
s3Password=<Replace with your S3 password>
s3DefaultFilePath=<Replace with your default path>
```

For more details, refer to the S3 plugin documentation:  
[https://github.com/lutece-platform/lutece-tech-plugin-s3](https://github.com/lutece-platform/lutece-tech-plugin-s3)

---

### 2. File Transfer

The transfer of files from the database to the S3 server is handled automatically by a Lutece daemon named `AttachmentRequestDaemon`.
This daemon must be enabled and properly configured to ensure the correct operation of the transfer.

---

### 3. RIC (Identity Management)

Access to RIC is required to retrieve and update user identity information.

Override the `attachment-request.properties` file with the following properties:

```properties
attachment-request.identitystore.ApiEndPointUrl=
attachment-request.identitystore.client.code=
```

---

## License

For open source projects, say how it is licensed.
