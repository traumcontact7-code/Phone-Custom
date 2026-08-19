# Lock Clock — Horloge grand format pour écran verrouillé (style MIUI)

Projet Android Studio complet (Kotlin) : un **Live Wallpaper** qui affiche
une horloge géante semi-transparente + la date, dans le style de la capture
d'écran (thème "SabriTN").

## Compiler l'application SANS Android Studio (dans le cloud, via navigateur)

Ce projet contient déjà tout ce qu'il faut (`.github/workflows/build.yml`)
pour que **GitHub compile l'APK à ta place**, gratuitement. Tu peux faire
tout ça depuis le navigateur de ton téléphone.

1. Crée un compte gratuit sur [github.com](https://github.com) (si tu n'en
   as pas déjà un).
2. Clique sur **"+"** en haut à droite → **"New repository"**.
   - Nom : `LockClockWallpaper` (ou ce que tu veux)
   - Laisse-le en **Public** (plus simple) ou **Private**, peu importe.
   - Clique **Create repository**.
3. Sur la page du dépôt vide, clique **"uploading an existing file"**
   (lien bleu au milieu de la page).
4. Décompresse le zip `LockClockWallpaper.zip` que je t'ai donné sur ton
   téléphone (avec une appli comme "Files" / "ZArchiver"), puis dans la
   page GitHub, fais glisser **tout le contenu du dossier** (tous les
   fichiers ET sous-dossiers : `app`, `.github`, `build.gradle.kts`,
   `settings.gradle.kts`, `README.md`, `.gitignore`) dans la zone
   d'upload. Sur mobile, utilise le bouton "choose your files" et
   sélectionne-les tous (les navigateurs récents gardent les
   sous-dossiers).
5. En bas de page, clique **"Commit changes"**.
6. Va dans l'onglet **"Actions"** en haut du dépôt. Une compilation
   ("Build APK") démarre automatiquement — ça prend 2 à 5 minutes.
7. Une fois le rond vert ✅ affiché, clique dessus, puis descends jusqu'à
   **"Artifacts"** en bas de page → télécharge **`LockClock-apk`**
   (c'est un `.zip` contenant `app-debug.apk`).
8. Décompresse ce zip sur ton téléphone pour récupérer `app-debug.apk`,
   puis appuie dessus pour l'installer (Android te demandera d'autoriser
   "Installer des applications inconnues" pour l'appli que tu utilises
   pour ouvrir le fichier — accepte).

Alternative si tu as un PC : installe [Android Studio](https://developer.android.com/studio),
ouvre le dossier `LockClockWallpaper`, branche ton téléphone en USB
(débogage activé), clique ▶ Run.

## Utiliser l'application

1. Ouvre l'appli **Lock Clock** sur ton téléphone.
2. Bouton **"Définir comme fond d'écran"** → confirme.
3. Bouton **"Autoriser l'affichage sur écran verrouillé"** → dans la page
   qui s'ouvre (Réglages > Applications > Lock Clock > Autorisations),
   active :
   - **Afficher sur écran verrouillé**
   - **Démarrage automatique**
   - **Afficher les fenêtres popup pendant l'exécution en arrière-plan**
4. Bouton **"Désactiver l'optimisation batterie"** → confirme, sinon MIUI
   risque de couper l'appli en arrière-plan et l'horloge se figera.

## Important à savoir sur Xiaomi/MIUI

- MIUI ne permet **pas** à une application tierce de remplacer entièrement
  l'écran de déverrouillage système (code PIN / empreinte / visage) — c'est
  une restriction de sécurité du système, pas une limite du code.
- Ce que fait cette appli : un **Live Wallpaper**, visible en fond sur
  l'écran d'accueil, et sur l'écran verrouillé **si** tu actives
  l'autorisation MIUI correspondante (étape 3 ci-dessus). C'est la même
  méthode que les vrais thèmes MIUI/HyperOS utilisent en coulisses.
- Certaines versions de MIUI/HyperOS renomment légèrement ces réglages
  ("Autorisations spéciales", "Autres autorisations"...) — cherche
  "écran verrouillé" dans la barre de recherche des réglages si tu ne
  trouves pas l'option exacte.

## Personnaliser

- Le style (police, taille, couleur, position) se modifie dans
  `app/src/main/java/com/sabritn/lockclock/ClockWallpaperService.kt`,
  fonction `drawClock()`.
- Le dégradé de fond se modifie dans la fonction `drawBackground()` du même
  fichier — remplace-le par ta propre photo si tu veux (charge un
  `Bitmap` depuis les ressources ou la galerie et dessine-le avec
  `canvas.drawBitmap(...)` avant de dessiner l'horloge par-dessus).
