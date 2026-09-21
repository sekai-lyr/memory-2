# Sekai 商城设计记录

## Direction

Sekai is presented as a collector's archive that can open into a market. The visual seed is a vermilion washi square becoming an orizuru through deliberate folds. That idea becomes a paper-white canvas, ink typography, sharp ruled sections, and red fold marks that guide the next action.

The visual direction is intentionally specific to a secondhand anime collectible marketplace: restrained editorial composition, real catalog imagery, Japanese and Chinese naming preserved, and clear inventory language. It avoids generic retail gradients, glass panels, decorative dashboards, and fabricated social proof.

## Design tokens

- Paper: `#f2eee6`
- Surface: `#fbfaf7`
- Ink: `#191a1d`
- Muted ink: `#716d67`
- Vermilion accent: `#b52b25`
- Success: `#2d6e57`
- Borders: `#d4cec3` and `#bcb3a7`
- Soft shadow: `0 10px 30px rgba(54, 44, 32, 0.08)`
- Display type: Georgia with Songti fallback
- Interface type: Segoe UI with PingFang SC and Microsoft YaHei fallbacks

The accent is reserved for primary actions, price, status, selected tabs, and the fold-like top rule on summaries and dialogs. Corners are mostly square or lightly rounded so the page feels assembled from paper sheets rather than product cards floating in glass.

## Page system

- Discovery: asymmetric hero with copy and search on the left, generated collector's desk still life on the right, then category rules and a real product grid.
- Product detail: image-first gallery, compact purchase rail, metadata rows, local detail images, seller provenance, and an honest simulated payment label.
- Publish and edit: two-column form with the main content on the left and a sticky paper note on the right.
- Cart and orders: ruled lists, visible totals, reversible state changes, and explicit loading, empty, and error states.
- Auth: dark ink introduction panel paired with a quiet form sheet.

## Motion

The shared `common.js` uses native browser Web Animations because the project has no frontend package pipeline. Intro reveals use a short upward settle with a cubic-bezier easing, while CSS handles toast, dialog, and state transitions. Motion is reduced or removed when `prefers-reduced-motion` is enabled. Dynamic catalog content remains usable without motion.

## Responsive behavior

The main content is capped at 1280px with 20px gutters. The hero, detail rail, form rail, and cart summary collapse into one column below 1024px. Product cards become two columns on narrow screens. Navigation reduces to essential links and hides utility labels below 520px. Cart rows keep the checkbox, image, copy, quantity, subtotal, and remove action visible at narrow widths.

## Assets

`src/main/resources/static/assets/hero-collection-desk.png` is the generated editorial still life used in the discovery hero. Its `impeccable:prompt` PNG metadata preserves the generation intent. Product cards and detail pages use the existing local image paths from `uploads/anime/` and `uploads/anime2/`, so the catalog remains grounded in repository content.

## Review status

The page templates, shared CSS, browser scripts, and asset provenance have been checked. Maven packaging passes and all inline and external JavaScript files parse successfully. A live Spring Boot smoke check was attempted, but the local JVM failed to establish its loopback selector before the app could listen on port 8082, so browser screenshots could not be captured in this environment.
