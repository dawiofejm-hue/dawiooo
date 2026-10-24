BUDOWANIE PLUGINÓW PRZEZ GITHUB ACTIONS – konfiguracja (jednorazowo)

1. Na GitHubie utwórz PRYWATNE repozytorium, np. "dawio-builder".
2. Wgraj do niego zawartość tego folderu (github-builder/): pom.xml, scripts/job.py oraz .github/workflows/build.yml
   (folder .github jest ukryty – przy wgrywaniu przez przeglądarkę użyj "Add file -> Upload files" i przeciągnij cały folder,
   albo utwórz plik ręcznie: "Create new file" i wpisz nazwę .github/workflows/build.yml).
3. Settings -> Actions -> General -> Workflow permissions: wybierz "Read and write permissions" -> Save.
4. Wygeneruj token: GitHub -> Settings -> Developer settings -> Personal access tokens -> Fine-grained tokens -> Generate:
     - Repository access: Only select repositories -> dawio-builder
     - Permissions (Repository): Contents = Read and write, Actions = Read and write
5. Wpisz do konfiguracji strony:
     PHP (config.php):  'github_token' => 'github_pat_...', 'github_repo' => 'TwojLogin/dawio-builder'
     Node (.env):       GITHUB_TOKEN=github_pat_...   GITHUB_REPO=TwojLogin/dawio-builder
   Opcjonalnie: github_branch / GITHUB_BRANCH (domyślnie main), build_per_hour / BUILD_PER_HOUR (domyślnie 8 na osobę).
6. Token trzymaj tylko w config.php / .env. Nigdy w przeglądarce ani w repozytorium.

Limity: darmowe konto GitHub daje 2000 minut Actions miesięcznie dla prywatnych repo, jeden build to ok. 1-2 minuty.
