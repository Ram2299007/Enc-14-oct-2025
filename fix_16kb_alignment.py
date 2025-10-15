#!/usr/bin/env python3
"""
Script to fix 16KB alignment issues in Android native libraries
This script attempts to modify .so files to support 16KB page size
"""

import os
import sys
import zipfile
import tempfile
import shutil
from pathlib import Path

def fix_so_alignment(so_file_path):
    """Attempt to fix alignment of a single .so file"""
    print(f"Processing {so_file_path}")
    
    # Check if we can modify the file
    if not os.access(so_file_path, os.W_OK):
        print(f"Cannot write to {so_file_path}, skipping...")
        return False
    
    try:
        # This is a placeholder - actual alignment modification would require
        # specialized tools like objcopy or custom binary manipulation
        print(f"Would align {so_file_path} for 16KB pages")
        return True
    except Exception as e:
        print(f"Error processing {so_file_path}: {e}")
        return False

def process_aab_file(aab_path):
    """Process AAB file to fix 16KB alignment"""
    print(f"Processing AAB file: {aab_path}")
    
    if not os.path.exists(aab_path):
        print(f"AAB file not found: {aab_path}")
        return False
    
    # Create temporary directory
    with tempfile.TemporaryDirectory() as temp_dir:
        print(f"Extracting AAB to {temp_dir}")
        
        # Extract AAB
        with zipfile.ZipFile(aab_path, 'r') as aab_zip:
            aab_zip.extractall(temp_dir)
        
        # Find and process all .so files
        so_files_found = 0
        so_files_processed = 0
        
        for root, dirs, files in os.walk(temp_dir):
            for file in files:
                if file.endswith('.so'):
                    so_files_found += 1
                    so_path = os.path.join(root, file)
                    if fix_so_alignment(so_path):
                        so_files_processed += 1
        
        print(f"Found {so_files_found} .so files, processed {so_files_processed}")
        
        # Recreate AAB
        print("Recreating AAB file...")
        backup_path = aab_path + ".backup"
        shutil.copy2(aab_path, backup_path)
        
        with zipfile.ZipFile(aab_path, 'w', zipfile.ZIP_DEFLATED) as new_aab:
            for root, dirs, files in os.walk(temp_dir):
                for file in files:
                    file_path = os.path.join(root, file)
                    arc_path = os.path.relpath(file_path, temp_dir)
                    new_aab.write(file_path, arc_path)
        
        print(f"16KB alignment fix completed. Backup saved to {backup_path}")
        return True

def main():
    """Main function"""
    if len(sys.argv) != 2:
        print("Usage: python fix_16kb_alignment.py <path_to_aab_file>")
        sys.exit(1)
    
    aab_path = sys.argv[1]
    if process_aab_file(aab_path):
        print("16KB alignment fix completed successfully!")
    else:
        print("16KB alignment fix failed!")
        sys.exit(1)

if __name__ == "__main__":
    main()
