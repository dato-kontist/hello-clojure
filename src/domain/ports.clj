(ns domain.ports)

(defprotocol UserRepositoryPort
  (find-by-email [this email]
    "Finds a user by its email.")
  (create [this user]
    "Creates a register of the given user"))