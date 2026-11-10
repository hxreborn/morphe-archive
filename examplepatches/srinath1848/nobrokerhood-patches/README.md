# 👋🧩 Morphe Patches template

Template repository for Morphe Patches.

## ❓ About

Patches for apps I like.

<!-- TODO: Update this about section with a brief introduction/summary about this repo and what it offers. -->

### How to use these patches

Click the button below, or manually add this repo url to Morphe Manager: https://github.com/srinath1848/nobrokerhood-patches

<a href="https://morphe.software/add-source?github=srinath1848/nobrokerhood-patches">
  <img src=".github/assets/add-to-morphe.svg" alt="Add to Morphe" height="48">
</a>

## 🩹 Patches list

<!-- PATCHES_START EXPANDED -->
> **[v1.1.0](https://github.com/srinath1848/nobrokerhood-patches/releases/tag/v1.1.0)**&nbsp;&nbsp;•&nbsp;&nbsp;`main`&nbsp;&nbsp;•&nbsp;&nbsp;7 patches total
<details open>
<summary>📦 NoBrokerHood&nbsp;&nbsp;•&nbsp;&nbsp;7 patches</summary>
<br>

**Package name:** `com.app.nobrokerhood`

**🎯 Supported versions:**

| 3.6.11 |
| :---: |

| 💊&nbsp;Patch | 📜&nbsp;Description | ⚙️&nbsp;Options |
|----------|----------------|-----------|
| [NoBrokerHood: Hide app-bar banners (experimental)](#nobrokerhood-hide-app-bar-banners-experimental) | Empty app-bar banner entries; may leave a layout placeholder. |  |
| [NoBrokerHood: Hide expanded notification images (experimental)](#nobrokerhood-hide-expanded-notification-images-experimental) | Hide large banner images in entry/exit notifications while retaining text (3.6.11). |  |
| [NoBrokerHood: Hide feed ad cards](#nobrokerhood-hide-feed-ad-cards) | Hide forum/home-feed ad cards (3.6.11). |  |
| [NoBrokerHood: Hide homepage top ads (experimental)](#nobrokerhood-hide-homepage-top-ads-experimental) | Supply an empty top-ad list and disable its loading/expanded state. |  |
| [NoBrokerHood: Hide post-approval ad card (experimental)](#nobrokerhood-hide-post-approval-ad-card-experimental) | Hide the sponsored card shown after entry approval or pre-approval (3.6.11). |  |
| [NoBrokerHood: Hide promotional popups](#nobrokerhood-hide-promotional-popups) | Suppress app-level marketing dialogs (3.6.11). |  |
| [NoBrokerHood: Hide visitor-flow ads](#nobrokerhood-hide-visitor-flow-ads) | Hide the visitor-flow promotional carousel (3.6.11). |  |

</details>

<!-- PATCHES_END -->

### 🛠️ Building locally

- Run `./gradlew buildAndroid`
- The built patches .mpp file is found in `patches/build/libs/patches-*.mpp`
- Patch the mpp file using [Morphe-Desktop](https://github.com/MorpheApp/morphe-desktop)
  like any other patch bundle.

See the [Morphe documentation](https://github.com/MorpheApp/morphe-documentation) for more information.

## 📜 License

UserXYZ Patches are licensed under the [GNU General Public License v3.0](LICENSE)

Experimental NoBrokerHood 3.6.6 ad-removal patches.
