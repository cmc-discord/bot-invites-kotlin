CREATE TABLE IF NOT EXISTS application
(
	id        uuid PRIMARY KEY,
	applicant BIGINT                         NOT NULL,
	code      uuid                           NULL,
	"state"   VARCHAR(20) DEFAULT 'OPEN'     NOT NULL,
	thread_id BIGINT                         NULL,
	questions JSON        DEFAULT '{}'::json NOT NULL
);
