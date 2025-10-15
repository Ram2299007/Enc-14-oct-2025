#!/bin/bash

# Script to align native libraries for 16KB page size
# This script attempts to modify .so files to support 16KB alignment

AAB_FILE="$1"
TEMP_DIR=$(mktemp -d)

echo "Processing AAB file: $AAB_FILE"
echo "Temporary directory: $TEMP_DIR"

# Extract AAB
echo "Extracting AAB file..."
unzip -q "$AAB_FILE" -d "$TEMP_DIR"

# Find and process all .so files
echo "Processing native libraries..."
SO_COUNT=0
PROCESSED_COUNT=0

find "$TEMP_DIR" -name "*.so" -type f | while read -r so_file; do
    SO_COUNT=$((SO_COUNT + 1))
    echo "Processing: $(basename "$so_file")"
    
    # Check if we can modify the file
    if [ -w "$so_file" ]; then
        # Attempt to align for 16KB using objcopy if available
        if command -v objcopy >/dev/null 2>&1; then
            # Create a backup
            cp "$so_file" "$so_file.backup"
            
            # Try to align the library
            if objcopy --change-section-alignment .text=16384 \
                      --change-section-alignment .rodata=16384 \
                      --change-section-alignment .data=16384 \
                      "$so_file" "$so_file" 2>/dev/null; then
                echo "  ✓ Aligned $(basename "$so_file") for 16KB"
                PROCESSED_COUNT=$((PROCESSED_COUNT + 1))
            else
                echo "  ✗ Failed to align $(basename "$so_file")"
                # Restore backup
                mv "$so_file.backup" "$so_file"
            fi
        else
            echo "  ! objcopy not available, skipping $(basename "$so_file")"
        fi
    else
        echo "  ! Cannot write to $(basename "$so_file"), skipping"
    fi
done

echo "Processed $PROCESSED_COUNT out of $SO_COUNT native libraries"

# Recreate AAB
echo "Recreating AAB file..."
cd "$TEMP_DIR"
zip -r "$AAB_FILE" . >/dev/null 2>&1

# Cleanup
rm -rf "$TEMP_DIR"

echo "16KB alignment process completed"
echo "Note: Some third-party libraries may still show 4KB alignment warnings"
echo "This is a known limitation that affects many Android applications"
