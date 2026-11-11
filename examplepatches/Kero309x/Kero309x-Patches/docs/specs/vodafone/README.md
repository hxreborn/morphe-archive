# Ana Vodafone behaviour specs

Target application: `com.emeint.android.myservices`, version 2026.11.1 (version
code 1246), distributed as an APKM bundle.

| # | Behaviour | Spec | Default | Risk |
|---|---|---|---|---|
| 1 | Skip the store licence check | [license-check.md](license-check.md) | yes | high |
| 2 | Disable the forced update requirement | [forced-update.md](forced-update.md) | yes | medium |
| 3 | Disable usage tracking | [tracking.md](tracking.md) | yes | low |
| 4 | Ignore the server's remote wipe and forced logout | [remote-actions.md](remote-actions.md) | yes | medium |
| 5 | Hide the promotional card | [ad-space.md](ad-space.md) | yes | low |
| 6 | Remove the saved account limit | [account-limit.md](account-limit.md) | yes | low |
| 7 | Disable certificate pinning | [certificate-pinning.md](certificate-pinning.md) | yes | medium |
| 8 | Keep the password of each account | [stored-password.md](stored-password.md) | yes | medium |
| 9 | Allow screenshots | [screenshots.md](screenshots.md) | yes | low |

## Status

All nine specs are `derived` and implemented. The first release for this
application stopped at the two behaviours that decide whether a repackaged build
runs at all. The later ones cover what the application reports, what a push
message can make it do to the installation, the promotional card it draws, how
many accounts it keeps, the certificate it pins, the password it remembers and
the screens it hides from screenshots.

`derived` means the behaviour has a spec, a record of how it was derived and an
implementation that builds and applies to the stock APK. It does not mean it has
been checked on a device: no test device is available to this project yet, so
every spec still carries unchecked acceptance criteria.

## Scope of this release

The stock build was examined for the behaviours users usually ask about, and the
examination found nothing to patch behind them:

- no advertising SDK is bundled, so there is no ad surface to remove;
- the application hides its screens from screenshots through a security library
  it embeds, which is what the screenshot behaviour changes;
- the device checks that library carries, for root, for a developer build, for
  an emulator and for a hooking framework, are never started in this build;
- there is no Play Integrity attestation; the `IntegrityManager` in the build is
  the application's own device-integrity helper, not a store gate.

No patch is shipped for a behaviour the stock application does not have. The
promotional card is the only advertisement the application draws itself: it is
offered by the server and belongs to the operator, so it is filed as promotion
rather than as advertising.

## Not included

Behaviours that exist in other collections but are absent from this project are
not listed here on purpose. If they are wanted, they are new work: a new spec,
new derivation, new implementation.
