# size: 269
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
addi $v0 $zero 8
addi $a1 $zero 32
la $t2 hp0
lw $a0 ($t2)
syscall
la $t3 hp1
lw $t3 ($t3)
la $t4 hp1
lw $t4 ($t4)
addi $t5 $zero 116
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 101
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 115
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 116
sb $t5 ($t4)
addi $t4 $t4 1
addi $t5 $zero 10
sb $t5 ($t4)
addi $t4 $t4 1
sb $zero ($t4)
strcmploop_:
lb $t6 ($t0)
lb $t7 ($t3)
beq $t6 $zero br0
beq $t7 $zero br1
slt $t8 $t6 $t7
bnez $t8 br1
slt $t8 $t7 $t6
bne $t8 $zero br1
addi $t0 $t0 1
addi $t3 $t3 1
j strcmploop_
br0:
beq $t7 $zero br2
j br1
br1:
addi $v1 $zero 0
j br3
br2:
addi $v1 $zero 1
br3:
beq $zero $v1 br5
la $t9 hp2
lw $t9 ($t9)
move $t4 $t6
move $t5 $t7
addi $t6 $zero 115
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 116
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 114
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 105
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 110
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 103
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 115
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 32
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 97
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 114
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 101
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 32
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 101
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 113
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 117
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 97
sb $t6 ($t9)
addi $t9 $t9 1
addi $t6 $zero 108
sb $t6 ($t9)
addi $t9 $t9 1
sb $zero ($t9)
addi $v0 $zero 4
la $a0 hp2
lw $a0 ($a0)
syscall
j br4
br5:
la $t7 hp3
lw $t7 ($t7)
addi $t8 $zero 115
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 116
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 114
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 105
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 110
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 103
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 115
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 32
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 97
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 114
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 101
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 32
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 78
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 79
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 84
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 32
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 101
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 113
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 117
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 97
sb $t8 ($t7)
addi $t7 $t7 1
addi $t8 $zero 108
sb $t8 ($t7)
addi $t7 $t7 1
sb $zero ($t7)
addi $v0 $zero 4
la $a0 hp3
lw $a0 ($a0)
syscall
j br4
br4:
add $sp $zero $fp
addi $v0 $zero 10
syscall
