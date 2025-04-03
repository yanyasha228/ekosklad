ALTER TABLE presence_matchers
    ADD COLUMN product_id BIGINT;

ALTER TABLE presence_matchers
    ADD CONSTRAINT fk_presence_matcher_product
        FOREIGN KEY (product_id) REFERENCES products (id)
            ON DELETE CASCADE;