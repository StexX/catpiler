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
SomeStuff.var2: .word 4
SomeStuff: .byte 8
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
main: 
add $fp $zero $sp
subu $sp $sp 4
lw $t0 0($fp)
la $t1 hp0
move $t0 $t1
la $t2 SomeStuff.var1
lw $t2 ($t2)
add $v1 $t0 $t2
lw $v1 ($v1)
move $t3 $v1
lw $v1 ($v1)
addi $v1 $zero 4
sw $v1 ($t3)
la $t4 SomeStuff.var2
lw $t4 ($t4)
add $v1 $t0 $t4
lw $v1 ($v1)
move $t5 $v1
lw $v1 ($v1)
addi $v1 $zero 6
sw $v1 ($t5)
la $t6 SomeStuff.var1
lw $t6 ($t6)
add $v1 $t0 $t6
lw $v1 ($v1)
move $t7 $v1
lw $v1 ($v1)
move $t8 $v1
move $t2 $t3
move $t3 $t5
move $t4 $t7
la $t9 SomeStuff.var2
lw $t9 ($t9)
add $v1 $t0 $t9
lw $v1 ($v1)
move $t5 $v1
lw $v1 ($v1)
mul $v1 $t8 $v1
addi $v0 $zero 1
move $a0 $v1
syscall
add $sp $zero $fp
addi $v0 $zero 10
syscall
