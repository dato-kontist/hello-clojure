(ns domain.ports)

(defprotocol UserRepositoryPort
  (find-by-email [this email]
    "Finds a user by its email.")
  (find-by-id [this id]
    "Finds a user by its id.")
  (create [this user]
    "Creates a register of the given user")
  (delete [this user]
    "Deletes the given user"))