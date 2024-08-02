# hello-clojure
Learning how to use Clojure.

## Setup

Install [Java](https://sdkman.io/install).

Install [Lein](https://leiningen.org/#install).

Install clj-kondo. `brew install borkdude/brew/clj-kondo`

## Usage

`lein run`

Create a user

```curl
curl -X POST http://localhost:3000 \
    -H "Content-Type: application/json" \
    -d '{
            "id": "123",
            "email": "test@example.com",
            "name": "Test User"
        }'
```

Expected Output
`{"id":"123","email":"test@example.com","name":"Test User"}`

Try the same request again, then the expected output should be (until restarting the server):
`{"error":"User already registered"}`

### Test

`lein test`

### Known Issues
```
Caused by: clojure.lang.ExceptionInfo: Conflict! Expected 0001-create-schema but 0002-create-users-table was applied. {:reason :ragtime.strategy/migration-conflict, :expected "0001-create-schema", :found "0002-create-users-table"}
```
Running local tests + local env can cause migrations conflicts.
> So far the solution is to delete the PSQL docker container.

## License

Copyright © 2024

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
