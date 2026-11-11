# Keep the password of each account

- **Id:** `vodafone.account.stored-password`
- **Status:** derived
- **Default enabled:** yes
- **Risk:** medium
- **Category:** Account and entitlements

## Observable behaviour

The stock build stores the password of every account, encrypted, in its own
database, and uses it to log in again when the account is opened. It also
forgets it: a login that carries no password of its own writes an empty
password over the stored one, and the next time that account is opened the
application asks for the password again. After patching, the password is taken
from the signed token the server sends, so the application keeps it and stops
asking.

## Preconditions

None. The password is part of the account record the server sends with every
login.

## Acceptance criteria

- [ ] The application asks for the password of an account only when that
      account has never had one, and not again after the first time.
- [ ] Switching to a saved account logs it in with the password it already
      knows.
- [ ] A password the user types is still used for the login it belongs to, and
      is still stored for the next time.
- [ ] The account is still stored encrypted; nothing about the storage changes.
- [ ] Turning the patch off restores the stock behaviour.

## Out of scope

The password the user types when the application does ask for it, and anything
the server decides about the account. The patch changes where the stored
password comes from; it does not change the login request itself.

## Limitations

- The password that is kept is the one the server puts in its own token. If a
  server stopped sending it, the application would ask for the password
  again, as it does in the stock build.
- The application stores the password in its own encrypted database. This
  patch does not change where that is or how it is encrypted; it only stops
  the stored copy from being overwritten with nothing. A login that carries a
  password of its own still writes that one, so the password the user typed is
  never lost.

## Open questions

- Does the application read the stored password back? Answered for this build:
  the credential login reads the password of the account it is asked to open and
  uses it when it is there, and the account list is built from the stored
  records, so the password reaches the point where it is used.

## Verification

1. Install the patched build and sign in with a password.
2. Open the same account again, and confirm the application does not ask for
   the password.
3. Switch to another account and back, and confirm the same.
4. Restart the application and confirm the account still opens without a
   prompt.
5. Disable the patch, rebuild and install, and confirm the stock behaviour
   returns.

## Provenance

- Spec author and date: Kero309x, 2026-11-10
- Derived: 2026-11-10 from the stock build. Derivation record:
  `docs/provenance/LOG.md`, section `vodafone.account.stored-password`.
