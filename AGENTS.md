# AGENTS.md

## Cursor Cloud specific instructions

This repo is an Electronic Seal Management System (电子签章管理系统) with two services:

- `springboot/` — Spring Boot 2.5.9 REST backend (Java 8, Maven, MyBatis-Plus), serves on port **9090**.
- `SealSystem/` — Vue 2 + Element UI frontend (Vue CLI 4.5), dev server on port **8080**. It talks to the backend at `http://<serverIp>:9090`, where `serverIp` is set in `SealSystem/public/config.js` (default `localhost`).

The update script already runs `npm install` for the frontend on startup. The notes below cover things that are NOT obvious and are NOT handled automatically.

### Toolchain (already installed in the environment)

- **Backend requires a JDK 8 that bundles JavaFX**, installed at `/opt/java/jdk8u504-full` (BellSoft Liberica "Full"). Standard OpenJDK 8/11/21 will **fail to compile** because `controller/FileController.java` imports an unused JavaFX-internal class (`com.sun.scenario.effect...`). The system default `java` is Java 21 — do not build the backend with it. Always build/run with:
  `export JAVA_HOME=/opt/java/jdk8u504-full; export PATH=$JAVA_HOME/bin:$PATH`
- **Frontend requires Node 16** (via nvm; `nvm use 16`). The default `node` on `PATH` (`/exec-daemon/node`) is v22 and is not appropriate for Vue CLI 4.5. `nvm alias default` is set to 16, but a fresh shell may still resolve the v22 binary first, so run `nvm use 16` (or prepend `$NVM_DIR/versions/node/v16.20.2/bin` to `PATH`) before `npm run serve`.

### MySQL (must be started manually each session)

- Start it with `sudo service mysql start` (systemd is not running here). It is **not** auto-started by the update script.
- Connection used by the backend: `jdbc:mysql://localhost:3306/mengs-bs`, user `root`, password `123`.
- The repo ships **no upstream SQL dump**; the schema + seed data were reconstructed in `springboot/db/init.sql`. It is idempotent. The database and seed data persist in the VM snapshot, so you normally don't need to re-run it. To (re)apply: `mysql -uroot -p123 -h127.0.0.1 mengs-bs < springboot/db/init.sql`.
- The `mysql` CLI over the unix socket fails with a permission error; always connect over TCP with `-h127.0.0.1`.
- Seeded admin login: username `admin`, password `123` (super admin, role_id 1). Also seeded roles `普通用户` (99) and `审批员` (100).

### Running the services

- Backend: from `springboot/`, with the JDK 8 env set, run `mvn spring-boot:run` (port 9090). `mvn test` also requires MySQL to be up (the only test is a Spring context load).
- Frontend: from `SealSystem/`, with Node 16, run `npm run serve` (port 8080). There is no lint script defined; only `serve` and `build` exist in `package.json`.

### App flow gotcha

- The frontend caches the role list into `localStorage["roleInfo"]` **only when the public landing page `http://localhost:8080/` is loaded** (see `views/front/Home.vue`). Several pages (e.g. creating a user in 用户管理, and `Person.vue`) call `roleInfo.forEach(...)`. If you navigate directly to `/login` without visiting `/` first, user creation throws `TypeError: roleInfo.forEach is not a function`. Always load `http://localhost:8080/` once before logging in and exercising role-dependent flows.
