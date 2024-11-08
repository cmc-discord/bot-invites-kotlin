CREATE TABLE IF NOT EXISTS "code"
(
	id         uuid PRIMARY KEY,

	owned_by   BIGINT                NOT NULL,
	used_by    BIGINT                NULL,

	created_at TIMESTAMP             NOT NULL,
	used_at    TIMESTAMP             NULL,

	used       BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE INDEX code_owned_by ON code (owned_by);

CREATE TABLE IF NOT EXISTS "user"
(
	id              BIGSERIAL PRIMARY KEY,

	invite_code     uuid          NULL,

	last_joined     TIMESTAMP     NOT NULL,
	last_seen       TIMESTAMP     NOT NULL,

	codes_remaining INT DEFAULT 0 NOT NULL
);

CREATE INDEX user_invite_code ON "user" (invite_code);
