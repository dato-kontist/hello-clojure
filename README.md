# hello-clojure

Learning how to use Clojure.

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

### Might be Useful

Install Java.
Install Lein.
Install clj-kondo. `brew install borkdude/brew/clj-kondo`

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
