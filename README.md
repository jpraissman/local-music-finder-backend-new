### How To Run (IntelliJ IDEA)

1. Make sure Docker Desktop is running
2. Run `docker compose up -d` from root
3. Start Application with Green run button (db migrations should automatically be run)


### How To Make DB Changes

1. Create/update the correct model in the `models` package
2. When ready, click "Persistence" on the left sidebar.
3. Towards bottom left, right-click "TheLocalMusicFinderBackend" --> "New" --> "Flyway Migration"
4. Follow Steps from there. Eventually, a new migration file should be added to `src/main/resources/db/migration`