# Kero309x Patches

An independent, clean-room patch collection for the Morphe patching framework,
maintained by Kero309x.

> **Status: pre-alpha.** All twenty patches are implemented and pass the build and
> the patch-application gate against the stock 8.0.28 build (20/20). None has been
> checked on a device yet. A signed pre-release bundle, `v0.1.2`, is published;
> treat it as unverified at runtime.

## What this is

A bundle of patches applied to the stock build of a supported application,
producing a repackaged build with a modified behaviour set. The bundle is used
with Morphe Manager, or with any tool that consumes a Morphe patch bundle.

The bundle is built for multiple applications. Each one is declared once in
`AppRegistry`, with its own compatibility data and behaviour specs, and adding
one is a package change rather than a core change. The table below lists the
applications currently registered.

## Install with Morphe

This repository is a patch source. Add it from Morphe Manager, or open this link
on an Android device:

- Open in Morphe: <https://morphe.software/add-source?github=Kero309x/Kero309x-Patches&name=Kero309x+Patches>
- Or paste the repository URL as a remote source:
  `https://github.com/Kero309x/Kero309x-Patches`
- Tools that take the metadata file directly can use
  `https://raw.githubusercontent.com/Kero309x/Kero309x-Patches/main/patches-bundle.json`

Third-party indexes read `patches-list.json` from the same branch. It lists every
patch with the application and version it targets, and is generated from the
catalogue rather than written by hand.

Morphe reads `patches-bundle.json` from `main` and downloads the bundle attached
to the latest release. Every `0.x` release is a pre-release, so enable
pre-releases for this source.

From `0.1.2` the bundle is signed with Ed25519 and the metadata publishes the
matching `public_key`, so the manager can reject a bundle altered after
publication. Earlier releases are unsigned and have no `classes.dex`, which makes
them Desktop-only. No patch has been checked on a device, so treat the bundle as
unverified at runtime.

## Independence

This project is not a fork, a mirror, or a rebranding of another patch
collection.

- The behaviour of each patch is specified in `docs/specs/` before any code is
  written, from the observable behaviour of the stock application.
- Every code point a patch touches is derived from the stock build, and the
  derivation is recorded in `docs/provenance/LOG.md`.
- No source, comment, identifier, helper, fingerprint or folder structure is
  taken from another patch project.
- No other patch project is a build dependency.

The framework itself is used through its published contracts only. See
`NOTICE` for its licence terms and for the naming restriction that applies to
derivative works.

## Supported applications

| Application | Package | Version | File type |
|---|---|---|---|
| Anghami | `com.anghami` | 8.0.28 (8000280) | APKM |

The table lists the applications currently registered. The bundle is not tied to
a single application: another target is added as a package with its own
compatibility data and specs, without changing the core layer.

Behaviour specifications: [`docs/specs/anghami/`](docs/specs/anghami/README.md).

Behaviours that other collections implement but this one does not are not
carried over. Adding one is new work: a spec, a derivation, an implementation,
and a verification run.

## Repository layout

```
patches/                    patch bundle sources
  src/main/kotlin/app/keropatches/
    core/                   application model, categories, shared rules
    registry/               the list of supported applications
    <application>/          compatibility data and patches, per application
docs/
  architecture/             how the bundle is put together
  decisions/                architecture decision records
  provenance/               clean-room protocol and derivation log
  specs/                    behaviour specifications, per application
.github/workflows/          build and release
```

## Building locally

Requirements:

- JDK 21
- A GitHub personal access token with `read:packages`, because the framework
  plugin is published to GitHub Packages.

Provide the registry credentials outside the repository, for example in
`~/.gradle/gradle.properties`:

```properties
gpr.user=your-github-username
gpr.key=your-token
```

Then:

```bash
./gradlew build
```

The Gradle wrapper is committed and pins the Gradle version the project is built
with.

## Verifying a patch

Patches are applied against a stock build that must be provided locally. The build
never downloads, uploads or redistributes it:

```bash
./gradlew verifyPatches -Papk="path/to/stock-build.apk"
```

Every registered patch is applied with validation enabled, and the result must
assemble. Add `-Pout="path/to/patched.apk"` to write the patched, unsigned APK.

This is the acceptance gate for every patch. It proves the fingerprints match and
the bytecode assembles; runtime behaviour needs a device and is checked separately.

## Catalogue

The table below mirrors the ordered `Catalogue.kt` list and is updated in the same
change as any patch. The registry test keeps the catalogue, the registry and the
behaviour specs in agreement. All twenty patches are enabled by default (decision
`0004`).

| # | Patch | Category | Default | Spec |
|---|---|---|---|---|
| 1 | Block ads between tracks | Ads and promotion | on | [spec](docs/specs/anghami/audio-ads.md) |
| 2 | Unlimited track skips | Playback | on | [spec](docs/specs/anghami/track-skips.md) |
| 3 | Unlock lyrics | Media and lyrics | on | [spec](docs/specs/anghami/lyrics-access.md) |
| 4 | Disable forced shuffle | Playback | on | [spec](docs/specs/anghami/forced-shuffle.md) |
| 5 | Hide shuffle badges | Interface | on | [spec](docs/specs/anghami/shuffle-badges.md) |
| 6 | Hide premium feature buttons | Interface | on | [spec](docs/specs/anghami/premium-buttons.md) |
| 7 | Allow screenshots | Interface | on | [spec](docs/specs/anghami/screenshots.md) |
| 8 | Show profile Plus badge | Account and entitlements | on | [spec](docs/specs/anghami/profile-plus-badge.md) |
| 9 | Plus entitlement state | Account and entitlements | on | [spec](docs/specs/anghami/plus-entitlement.md) |
| 10 | Hide Gold upsell | Store and upsells | on | [spec](docs/specs/anghami/gold-upsell.md) |
| 11 | Hide upgrade banners | Store and upsells | on | [spec](docs/specs/anghami/upgrade-banners.md) |
| 12 | Lift download limits | Account and entitlements | on | [spec](docs/specs/anghami/download-limits.md) |
| 13 | Disable analytics | Privacy | on | [spec](docs/specs/anghami/analytics.md) |
| 14 | Hide sponsored content | Ads and promotion | on | [spec](docs/specs/anghami/sponsored-content.md) |
| 15 | Block promotional popups | Ads and promotion | on | [spec](docs/specs/anghami/promotional-popups.md) |
| 16 | Disable the rating prompt | Store and upsells | on | [spec](docs/specs/anghami/rating-prompt.md) |
| 17 | Stock signature assertion | Account and entitlements | on | [spec](docs/specs/anghami/signature-assertion.md) |
| 18 | Hide in-feed ad banners | Ads and promotion | on | [spec](docs/specs/anghami/feed-ad-banners.md) |
| 19 | Ignore the forced update requirement | Store and upsells | on | [spec](docs/specs/anghami/forced-update.md) |
| 20 | Disable first-party Silo telemetry | Privacy | on | [spec](docs/specs/anghami/silo-telemetry.md) |

## Development

- [Development setup](docs/development/setup.md)
- [Architecture](docs/architecture/overview.md)
- [Clean-room protocol](docs/provenance/CLEAN_ROOM.md)
- [Decision records](docs/decisions/)
- [Contributing](CONTRIBUTING.md)

## Licence

GPL-3.0-only. See [`LICENSE`](LICENSE) and [`NOTICE`](NOTICE).

## Not affiliated

Not affiliated with, endorsed by, or sponsored by any application vendor, nor by
the Morphe project. Application names and icons belong to their owners and are
used only to describe which stock build a patch targets.
