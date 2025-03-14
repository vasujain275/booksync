CREATE TABLE IF NOT EXISTS "book_review" (
    id UUID PRIMARY KEY,
    book_id UUID NOT NULL,
    user_id UUID NOT NULL,
    rating INTEGER NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_book_review_book FOREIGN KEY(book_id) REFERENCES "book"(id),
    CONSTRAINT fk_book_review_user FOREIGN KEY(user_id) REFERENCES "user"(id)
);
