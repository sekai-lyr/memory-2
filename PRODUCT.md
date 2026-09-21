# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Stack

Existing Spring Boot + MyBatis + Thymeleaf application. Frontend direction delegated to this build: preserve the server-rendered templates and JSON APIs, use native CSS and browser JavaScript without introducing a separate frontend build pipeline.

## Users

Primary users are anime and game collectible buyers browsing independent listings, comparing item details, and completing a purchase. Secondary users are small sellers who publish and manage their own listings and fulfill buyer orders.

## Product Purpose

Sekai is a focused marketplace for anime and game collectibles. It supports discovery, product detail inspection, cart and checkout, order lifecycle management, and seller-side inventory and fulfillment.

## Positioning

The marketplace is centered on specific collectible formats and fandoms rather than generic retail categories. Its useful mechanism is the full buyer-to-seller loop for items such as acrylic stands, figures, badges, art books, CDs, apparel, and fan goods.

## Operating Context

Buyers browse a large seed catalog, filter by collectible category, inspect local product imagery, add items to a cart, submit receiver details, pay through the simulated payment flow, and confirm delivery. Sellers publish listings with image URLs or uploads, then ship paid orders.

## Capabilities and Constraints

- Existing page routes: `/product/list`, `/product/detail?id=`, `/product/publish`, `/product/edit`, `/product/mine`, `/trade/cart`, `/trade/orders`, `/trade/seller/orders`, `/login`, and `/reg`.
- Existing APIs cover authentication, categories, catalog pagination, product publishing and editing, cart actions, checkout, payment, shipping, confirmation, cancellation, and image upload.
- The catalog is seeded with anime and game collectible imagery under `uploads/anime/` and `uploads/anime2/`.
- Authentication uses a server session. Registration logs the user in.
- Order and payment behavior is simulated. The UI must label this clearly and must not imply real payment processing.
- The current server default is port 8082 and the database is MySQL.
- Existing response payloads may expose both `success` and `isSuccess`; client code should accept either.

## Brand Commitments

- The product name is Sekai 商城.
- Product language is Chinese with Japanese and English item names preserved when they are part of the listing.
- Existing product imagery and product names are source content, not decorative placeholders.
- The new interface should feel like a collector's archive and market, not a generic department store.

## Evidence on Hand

- `README.md` documents the buyer and seller workflows and the 1000-item anime catalog.
- `data-build/upgrade-v3-anime.sql` documents the six top-level collectible categories and their subcategories.
- `data-build/upgrade-v4-mass.sql` contains seeded product names, descriptions, prices, stock, sellers, and image paths.
- `uploads/anime/` and `uploads/anime2/` contain local product images. No verified testimonials, real sales metrics, or live payment provider are present and the interface must not fabricate them.

## Product Principles

1. Let the collectible image and item name lead discovery.
2. Make provenance, availability, and next action obvious.
3. Keep buying and selling flows reversible until the final action.
4. Treat fandom categories as useful navigation, not decorative labels.

## Accessibility & Inclusion

Use semantic landmarks, explicit form labels, visible keyboard focus, readable contrast, reduced-motion fallbacks, mobile layouts below 768px, and honest empty, loading, and error states. Do not rely on emoji as the only meaning-bearing icon.

## Inferred Product Facts

The primary audience, marketplace positioning, and product principles above are structured inferences from the repository and the user's request because no answer round was available in this session. They should be confirmed by the product owner before a production launch.
