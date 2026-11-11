# Remove the saved account limit

- **Id:** `vodafone.account.account-limit`
- **Status:** derived
- **Default enabled:** yes
- **Risk:** low
- **Category:** Account and entitlements

## Observable behaviour

The stock build keeps at most five accounts, or six when the first of them is a
seamless one. Once the list is that long the application refuses to switch to
another account and shows a screen that says too many accounts are saved.
After patching, the list has no limit: every account that is saved can be
opened, and new accounts can be added.

## Preconditions

None. The limit is checked in the application itself, against the list of
accounts it has saved.

## Acceptance criteria

- [ ] A sixth account can be added and used.
- [ ] Switching to any saved account works, however many accounts are saved.
- [ ] The screen that says too many accounts are saved is not shown.
- [ ] The accounts behave as before: they are logged in, switched and removed
      the same way.
- [ ] Turning the patch off restores the limit.

## Out of scope

How many accounts the server accepts and what each account may do. The patch
removes the check in the application; it does not change what the server
answers.

## Limitations

- The accounts are stored the way they always were. The patch only removes the
  check, so nothing about how an account is remembered, logged in or removed
  changes.
- The list of accounts is still what the application has saved. A very long
  list is shown in the same list as before.

## Open questions

- Is the limit enforced anywhere else? Answered for this build: the limit is
  counted in two places, the overlay that lists the accounts and the overlay
  that asks which account to switch to. Both are changed.

## Verification

1. Install the patched build and sign in.
2. Add accounts until there are more than five, and confirm that none of the
   steps refuses and that the "too many accounts" screen never appears.
3. Switch between the accounts and confirm that each one opens.
4. Disable the patch, rebuild and install, and confirm the limit returns.

## Provenance

- Spec author and date: Kero309x, 2026-11-10
- Derived: 2026-11-10 from the stock build. Derivation record:
  `docs/provenance/LOG.md`, section `vodafone.account.account-limit`.
