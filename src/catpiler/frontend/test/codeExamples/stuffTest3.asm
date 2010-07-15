# size: 120
.data
hp0: .space 4
hp1: .space 4
hp2: .space 4
hp3: .space 4
hp4: .space 4
hp5: .space 4
hp6: .space 4
hp7: .space 4
hp8: .space 4
hp9: .space 4
hp10: .space 4
hp11: .space 4
hp12: .space 4
hp13: .space 4
hp14: .space 4
SomeStuff.var1: .word 0
SomeStuff: .byte 4
.text
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp0
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp1
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp2
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp3
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp4
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp5
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp6
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp7
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp8
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp9
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp10
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp11
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp12
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp13
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp14
sw $v0 ($t0)
lw $t0 SomeStuff.var1($fp)
la $t0 hp0
lw $t0 ($t0)
main: 
add $fp $zero $sp
subu $sp $sp 4
lw $t1 0($fp)
la $t2 hp1
move $t1 $t2
la $t3 SomeStuff.var1
lw $t3 ($t3)
add $v1 $t1 $t3
lw $v1 ($v1)
move $t4 $v1
lw $v1 ($v1)
la $t5 hp2
lw $t5 ($t5)
addi $t6 $zero 116
sb $t6 ($t5)
addi $t5 $t5 1
addi $t6 $zero 101
sb $t6 ($t5)
addi $t5 $t5 1
addi $t6 $zero 115
sb $t6 ($t5)
addi $t5 $t5 1
addi $t6 $zero 116
sb $t6 ($t5)
addi $t5 $t5 1
sb $zero ($t5)
la $t7 hp2
move $v1 $t7
sw $v1 ($t4)
la $t8 SomeStuff.var1
lw $t8 ($t8)
add $v1 $t1 $t8
lw $v1 ($v1)
move $t9 $v1
lw $v1 ($v1)
addi $v0 $zero 4
move $a0 $v1
syscall
add $sp $zero $fp
addi $v0 $zero 10
syscall
