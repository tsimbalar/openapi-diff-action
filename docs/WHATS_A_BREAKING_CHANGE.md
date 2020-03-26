# What is a "Breaking Change"?

When developing a REST API, a "breaking change" is any change made to that API which could potentially cause an existing client to encounter an error, crash, or otherwise "break". It is important for API developers to understand what types of changes constitute "breaking" behavior and how to manage these changes.

## Common Examples of Breaking Changes

This action relies on a 3rd-party library, [quen2404/openapi-diff](https://github.com/quen2404/openapi-diff) to compare OpenAPI spec files and classify changes as breaking or non-breaking. The library currently contain documentation outlining which changes are supported. This section aims to provide common examples of breaking changes.

### Removal of an Endpoint

> _Example: "The `GET /v1/pets` endpoint was completely removed."_

Removal of an endpoint is always a breaking change, even if the endpoint was previously marked as _deprecated_. If an existing client attempts to call a removed endpoint, it will result in a `404: Not Found` error. As a non-breaking alternative, consider marking the endpoint as _deprecated_. Deprecated endpoints should be removed only after verifying that most (if not all) existing clients are no longer using them.

### Modification of an Endpoint's HTTP Verb, Path, Accepted Media Type(s) or Produced Media Type(s)

> _Example: "The `GET /v1/pets` endpoint was changed to `POST /v1/pets`."_

> _Example: "The `GET /v1/pets` endpoint was changed to `GET /v1/companies/{companyId}/pets`."_

> _Example: "The `GET /v1/pets` endpoint previously accepted `application/json` but now only accepts `application/xml`."_

Changing an endpoint's HTTP verb, path, consumed media type(s) or consumed media type(s) is essentially the same thing as removing the endpoint and adding another one. If an existing client attempts to call an endpoint whose HTTP verb has been changed, it will result in a `404: Not Found` error.

### Addition of a Required Query Parameter or Header to an Existing Endpoint

> _Example: "The `GET /v1/pets` endpoint was changed to `GET /v1/pets?param1={value1}`, where `param1` is marked as 'required'."_

Adding a new query parameter or header to an endpoint is only a breaking change if the new parameter or header is marked as _required_. If an existing client attempts to call an endpoint without supplying the new parameter or header, it will result in a `400: Bad Request` error. As a non-breaking alternative, consider marking the new perameter or header as _optional_ and checking for its presence in your application code, or marking the endpoint as _deprecated_ and making a new endpoint with a new version.

### Addition of a Required Property to the Request Body of an Existing Endpoint

> _Example: "A new, required property `prop1` was added to the expected request body of the `GET /v1/pets` endpoint."_ 

Adding a required property to an incoming request body is similar to adding a required query parameter or header. If an existing client attempts to call an endpoint that was modified in this manner, it could produce a `400: Bad Request` or `500: Internal Server Error` response. As a non-breaking alternative, consider marking the new property as _optional_ and checking for its presence in your application code, or marking the endpoint as _deprecated_ and making a new endpoint with a new version.

### Changing an Optional Property to Required in the Request Body of an Existing Endpoint

> _Example: "An existing, optional property `prop1` was marked as 'required' in the request body of the `GET /v1/pets` endpoint."_

Marking an _optional_ property as _required_ has the same effect as adding a new, required property to an existing endpoint's request body. If an existing client attempts to call an endpoint that was modified in this manner, it could produce a `400: Bad Request` or `500: Internal Server Error` response. As a non-breaking alternative, consider marking the endpoint as _deprecated_ and making a new endpoint with a new version.


### Removal of a Required Property from the Response Body of an Existing Endpoint

> _Example: "A 'required' property `prop1` was removed from the response body of the `GET /v1/pets` endpoint."_

The OpenAPI spec allows developers to mark response body properties as required. The removal of one of these required properties constitutes a breaking change. If an existing client attempts to call an endpoint that was modified in this way, it could produce a client-side error such as a `NullPointerException`. As a non-breaking alternative, consider marking the endpoint as _deprecated_ and making a new endpoint with a new version. As a preventative measure, consider marking response body properties as _optional_ to begin with.

### Changing a Required Property to Optional in the Response Body of an Existing Endpoint

> _Example: "A 'required' property `prop1` was marked as 'optional' in the response body of the `GET /v1/pets` endpoint."_

Marking a _required_ property as _optional_ has the same effect as removing an existing, required proprty from an existing endpoint's response body. If an existing client attempts to call an endpoint that was modified in this way, it could produce a client-side error such as a `NullPointerException`. As a non-breaking alternative, consider marking the endpoint as _deprecated_ and making a new endpoint with a new version.

