ALTER TABLE "code"
	ADD note TEXT NULL;

UPDATE "code"
	SET note = NULL
	WHERE true;
