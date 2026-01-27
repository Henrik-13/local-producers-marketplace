-- Add producer_id column to Products table
-- This migration adds the producer foreign key column to link products to their producers
-- Handles both quoted ("Products") and unquoted (products) table names

-- Step 1: Determine the actual table name and add the column
DO $$
BEGIN
    -- Try quoted name first (case-sensitive)
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'Products') THEN
        ALTER TABLE "Products" ADD COLUMN IF NOT EXISTS producer_id BIGINT;
        RAISE NOTICE 'Added column to "Products" table';
    -- Try unquoted/lowercase name
    ELSIF EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'products') THEN
        ALTER TABLE products ADD COLUMN IF NOT EXISTS producer_id BIGINT;
        RAISE NOTICE 'Added column to products table';
    ELSE
        RAISE EXCEPTION 'Neither "Products" nor "products" table found. Please create the table first or restart Spring Boot to auto-create it.';
    END IF;
END $$;

-- Step 2: Update existing products to point to the first producer (if any exist)
DO $$
BEGIN
    -- Try quoted table names first
    IF EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'Products') THEN
        UPDATE "Products" 
        SET producer_id = (SELECT id FROM "Users" WHERE role = 'PRODUCER' LIMIT 1)
        WHERE producer_id IS NULL;
        
        -- Add foreign key constraint
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_product_producer') THEN
            ALTER TABLE "Products"
            ADD CONSTRAINT fk_product_producer 
            FOREIGN KEY (producer_id) REFERENCES "Users"(id);
        END IF;
    -- Try unquoted table names
    ELSIF EXISTS (SELECT FROM information_schema.tables WHERE table_schema = 'public' AND table_name = 'products') THEN
        UPDATE products 
        SET producer_id = (SELECT id FROM users WHERE role = 'PRODUCER' LIMIT 1)
        WHERE producer_id IS NULL;
        
        -- Add foreign key constraint
        IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_product_producer') THEN
            ALTER TABLE products
            ADD CONSTRAINT fk_product_producer 
            FOREIGN KEY (producer_id) REFERENCES users(id);
        END IF;
    END IF;
END $$;

-- Note: After running this script, restart your Spring Boot application
-- Hibernate's ddl-auto=update should then recognize the column exists
-- If the table doesn't exist yet, restart Spring Boot first to let Hibernate create it

