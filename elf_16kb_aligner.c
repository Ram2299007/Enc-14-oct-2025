/*
 * ELF 16KB Aligner
 * A C program to modify ELF headers for 16KB page size alignment
 * This is a simplified implementation for demonstration
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <elf.h>

#define PAGE_SIZE_16KB 16384

int modify_elf_for_16kb(const char* filename) {
    int fd = open(filename, O_RDWR);
    if (fd == -1) {
        perror("open");
        return -1;
    }
    
    // Get file size
    off_t file_size = lseek(fd, 0, SEEK_END);
    lseek(fd, 0, SEEK_SET);
    
    // Map file to memory
    void* file_data = mmap(NULL, file_size, PROT_READ | PROT_WRITE, MAP_SHARED, fd, 0);
    if (file_data == MAP_FAILED) {
        perror("mmap");
        close(fd);
        return -1;
    }
    
    // Check ELF header
    Elf64_Ehdr* ehdr = (Elf64_Ehdr*)file_data;
    if (memcmp(ehdr->e_ident, ELFMAG, SELFMAG) != 0) {
        printf("Not an ELF file: %s\n", filename);
        munmap(file_data, file_size);
        close(fd);
        return -1;
    }
    
    // Find program headers
    Elf64_Phdr* phdr = (Elf64_Phdr*)((char*)file_data + ehdr->e_phoff);
    
    // Modify LOAD segments to use 16KB alignment
    for (int i = 0; i < ehdr->e_phnum; i++) {
        if (phdr[i].p_type == PT_LOAD) {
            // Force 16KB alignment
            phdr[i].p_align = PAGE_SIZE_16KB;
            printf("Modified segment %d alignment to 16KB\n", i);
        }
    }
    
    // Sync changes
    msync(file_data, file_size, MS_SYNC);
    munmap(file_data, file_size);
    close(fd);
    
    printf("Successfully modified %s for 16KB alignment\n", filename);
    return 0;
}

int main(int argc, char* argv[]) {
    if (argc != 2) {
        printf("Usage: %s <elf_file>\n", argv[0]);
        return 1;
    }
    
    return modify_elf_for_16kb(argv[1]);
}
