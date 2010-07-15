# size: 191
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
subu $sp $sp 4
lw $t0 0($fp)
la $t0 hp0
lw $t0 ($t0)
lw $t1 -4($fp)
la $t1 hp1
lw $t1 ($t1)
la $t2 hp0
lw $t2 ($t2)
la $t3 hp0
lw $t3 ($t3)
addi $t4 $zero 104
sb $t4 ($t3)
addi $t3 $t3 1
addi $t4 $zero 97
sb $t4 ($t3)
addi $t3 $t3 1
addi $t4 $zero 105
sb $t4 ($t3)
addi $t3 $t3 1
sb $zero ($t3)
la $t5 hp1
lw $t5 ($t5)
la $t6 hp1
lw $t6 ($t6)
addi $t7 $zero 116
sb $t7 ($t6)
addi $t6 $t6 1
addi $t7 $zero 101
sb $t7 ($t6)
addi $t6 $t6 1
addi $t7 $zero 115
sb $t7 ($t6)
addi $t6 $t6 1
addi $t7 $zero 116
sb $t7 ($t6)
addi $t6 $t6 1
sb $zero ($t6)
addi $v1 $zero 0
move $t3 $t5
la $t4 hp3
lw $t4 ($t4)
addi $t5 $zero 115
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 116
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 114
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 105
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 110
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 103
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 115
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 32
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 97
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 114
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 101
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 32
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 78
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 79
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 84
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 32
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 101
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 113
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 117
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 97
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 108
sb $t5 ($t4)
addi $t4 $t4 1
sb $zero ($t4)
addi $v0 $zero 4
la $a0 hp3
lw $a0 ($a0)
syscall
add $sp $zero $fp
addi $v0 $zero 10
syscall
