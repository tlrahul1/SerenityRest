# FakeStore API Automation Framework

A clean clone of the MaxHealthcare **generic** automation framework (Style 2:
`Feature → CommonApiSteps → ApiManager → CommonUrlHandler → UrlResolverUtil →
BaseClass/Utilities → API`), repointed at the public FakeStore sandbox API
(`https://fakestoreapi.com`) instead of MaxHealthcare's backend.

Business/module-specific code (ADT, EMR, OT, CE, MMS, ER, Kitchen, Nursing) was
**not** carried over — only the reusable framework skeleton was cloned.

## Architecture map (original → this repo)

| Original | This repo | Role |
|---|---|---|
| `com.max.common.method.ApiManager` | `com.fakestore.common.method.ApiManager` | Generic HTTP executor |
| `com.max.common.steps.CommonApiSteps` | `com.fakestore.common.steps.CommonApiSteps` | Single Cucumber glue class for all APIs |
| `com.max.nursingapi.utilities.CommonUrlHandler` | `com.fakestore.utilities.CommonUrlHandler` | Endpoint registry for the Product API family |
| `com.max.nursingapi.utilities.UrlResolverUtil` | `com.fakestore.utilities.UrlResolverUtil` | Generic map-building / property resolution engine (byte-for-byte identical logic) |
| `BaseClass`, `ReusableSpecification`, `ResponseValidator`, `ResponseCode`, `TestUtils`, `JsonUtils`, `JsonLoader`, `ExcelReader`, `RestUtils`, `CommonSteps`, `TokenGenerator` | same names under `com.fakestore.utilities` | Same generic utility layer |
| `AdtApiRunner` / `AdtApiFailedTestRunner` | `FakeStoreApiRunner` / `FakeStoreApiFailedTestRunner` | Main suite runner + failed-scenario rerun runner |
| `serenity.conf` environments tree | same structure, `fakestore.api` + `product.master` | Environment-based URL resolution |
| `serenity.properties` `<key>.path=` registry | same convention | Endpoint key → JSON payload file mapping |

## Intentional deviations from the original (and why)

1. **No hardcoded credentials.** The original `TestUtils.java` had live-looking
   JWT bearer tokens hardcoded in source, and `TokenGenerator` returned
   hardcoded tokens. Both are reproduced here with the *same method shape*,
   but tokens are now resolved from `serenity.auth.token` (serenity.conf /
   environment variable) instead of being committed to the repo.
2. **`CommonSteps.verifyRequestSuccessful`** dropped the MaxHealthcare-specific
   field assertions (`data.returnMessage == "[Saved Successfully]"`, etc.)
   since those are business logic, not framework logic. It now does a generic
   HTTP-status check only — add back business assertions in your own step
   classes as needed.
3. **`ApiManager` gained `getApi`, `getApiWithQueryParams`, `putApi`,
   `putNegativeApi`, `deleteApi`** alongside the original's `postApi` /
   `postNegativeApi`. The original framework, as shipped, only had generic
   POST wired up end-to-end even though GET/PUT/DELETE were on your
   requirements list — these were added using the exact same
   `executeXRequest` private-method pattern already established by
   `executePostRequest`, no new design pattern introduced.
4. **Runner `glue` paths corrected.** The original runners (e.g.
   `ERApiRunner`) pointed `glue` at `com.max.common.methods` (plural) while
   the actual package is `com.max.common.method` (singular) — a harmless
   typo, since `ApiManager` has no Cucumber annotations and doesn't need to be
   on the glue path. This repo's runners just list
   `com.fakestore.common.steps`, which is all that's actually required.

## Adding a new API (the whole point of this architecture)

1. Add the base path to `serenity.conf` → `environments.all` if it's a new
   module family, or reuse `product.master` if it's another Product endpoint.
2. Add a `"logicalKey:/segment"` entry to the `UrlResolverUtil.mapTo(...)`
   call in `CommonUrlHandler`.
3. Add `<logicalKey>.path=/YourFolder/YourValid.json` to `serenity.properties`
   (POST/PUT only; GET/DELETE don't need one).
4. Drop the payload fixture under `src/test/resources/json_body/...`.
5. Write a `.feature` file using the existing generic step phrases — **no new
   Java code required.**

## Running

```bash
# default (sequential) profile
mvn clean verify -Psingle -Dclass.to.run=com.fakestore.runner.FakeStoreApiRunner

# parallel profile
mvn clean verify -Pparallel -Dclass.to.run=com.fakestore.runner.FakeStoreApiRunner -Dthread.count=2 -Dparallel.count=2

# re-run only failed scenarios from the previous run
mvn clean verify -Psingle -Dclass.to.run=com.fakestore.runner.FakeStoreApiFailedTestRunner
```

Serenity HTML report is aggregated to `target/site/serenity` after the
`post-integration-test` phase (same as the original).

## Note on the "negative" scenario

`fakestoreapi.com` is a public mock/sandbox API — it doesn't perform real
server-side validation and will return `200` for most payloads regardless of
content. The negative scenario in `CreateProduct.feature` is included to
demonstrate the framework's JSON-mutation + assertion mechanism
(`TestUtils.update_JsonArrayValueInteger` → `ApiManager.postNegativeApi` →
`ResponseValidator.verifyResponseMessage`), exactly as it would run against a
real, validating backend. Update the expected status/message once you point
this framework at real enterprise APIs.
