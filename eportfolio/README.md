
# CS499 ePortfolio (GitHub Pages)

This repository hosts a **static** ePortfolio for your CS499 capstone. It includes:
- `index.html` — Professional Self‑Assessment (put this first in the site)
- `code-review.html` — Embedded informal code‑review video
- `artifacts/` — Three enhancement pages: Software Engineering, Algorithms & Data Structures, Databases
- `assets/` — CSS/JS (includes a dark mode toggle)
- `.nojekyll` — Keeps the site purely static

## How to publish on GitHub Pages

1. Create a new GitHub repository (public is simplest for Pages).
2. Upload the contents of this folder to the repo root (not inside another folder).
3. Add your files to `files/`:
   - Export your milestone narratives to PDF and place them here as:
     - `Milestone Two.pdf`
     - `Milestone Three.pdf`
     - `Milestone Four.pdf`
   - Upload your code ZIPs here as:
     - `Milestone Two - code.zip`
     - `Milestone Three - code.zip`
     - `Milestone Four - code.zip`
4. Commit and push.
5. In **Settings → Pages**, set:
   - **Build and deployment**: *Deploy from a branch*
   - **Branch**: `main` (root `/`), then **Save**.
6. Wait for the `github-pages` deployment to finish.
7. Your site will be live at the URL shown in the Pages settings.

> If you prefer a custom domain, add it in **Settings → Pages** and create the required DNS record.

## Embed your code review video
- Upload the MP4 to YouTube (Unlisted), Vimeo, or Google Drive (shareable link).
- Open `code-review.html` and replace the `src` in the `<iframe>` with your embed URL.

## Replace placeholders
- Update text in `index.html` (self‑assessment) and each page in `artifacts/`.
- Add screenshots to `assets/` and reference them in the artifact pages.
- Link your GitHub repository where indicated.

## Accessibility / UX checks
- Provide alt text for images.
- Ensure keyboard focus is visible; links/buttons are reachable via Tab.
- Check contrast (especially if you customize colors).
- Verify pages work on mobile (narrow screen).

## Optional: Organize source code
You can keep **source projects** in separate repos and link to them from the artifact pages. Use the ZIPs in `/files` for the *submitted* versions to match course evidence.

---

**Tip:** Keep commit messages meaningful (e.g., `feat(db): add WAL + v2 migration`), and add a short `CHANGELOG.md` if you iterate during review.
