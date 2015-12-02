package com;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author <a href="mailto:wangxp@59store.com">乔巴</a>
 * @veresion 1.0 15/12/1
 * @since 1.0
 */
public class Calculator {

    /**
     * 运算符优先级比较,
     * a比b优先级高则返回1,优先级相同返回0,否则返回-1
     * @param a
     * @param b
     * @return int
     */
    private int compareOper(Object a, Object b) {
        if(a == null || b == null) {
            return Integer.MIN_VALUE;
        }
        String oneOper = "*/";
        String twoOper = "+-";
        if(oneOper.contains(a.toString()) && twoOper.contains(b.toString())) {
            return 1;
        }
        if((oneOper.contains(a.toString()) && oneOper.contains(b.toString()) ||
                (twoOper.contains(a.toString()) && twoOper.contains(b.toString())))) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * 判断是否为运算符
     * @param oper
     * @return
     */
    private boolean isOper(Object oper) {
        String str = "+-*/";
        if(str.contains(oper.toString())) {
            return true;
        }
        return false;
    }

    /**
     * 中缀表达式转后缀表达式
     */
    public Queue poland(Queue queue) {
        Queue q = new LinkedList<>();  //后缀表达式
        if(queue == null || queue.isEmpty()) {
            return q;
        }

        Stack s = new Stack();    //运算符
        while(queue.peek() != null) {
            Object obj = queue.poll();
            if(obj instanceof Number) {
                q.offer(obj);
            } else {
                if(s.isEmpty()) {
                    s.push(obj);
                } else {
                    Object stackTop = s.peek();
                    if(obj.toString().equals("(")) {
                        s.push(obj);
                    } else if(obj.toString().equals(")")) {
                        while(!s.isEmpty() && !s.peek().toString().equals("(")) {
                            Object tmp = s.pop();
                            if(!tmp.toString().equals("(")) {
                                q.offer(tmp);
                            }
                        }
                    } else if(this.compareOper(obj, stackTop) == 1) {
                        s.push(obj);
                    } else if(this.compareOper(obj, stackTop) == 0) {
                        q.offer(s.pop());
                        s.push(obj);
                    } else if(this.compareOper(obj, stackTop) == -1){
                        Object t = s.peek();
                        if(t.toString().equals("(")) {
                            s.push(obj);
                        } else {
                            while (!s.isEmpty()) {
                                Object tmp = s.pop();
                                if(!tmp.toString().equals("(")) {
                                    q.offer(tmp);
                                }
                            }
                            s.push(obj);
                        }
                    }
                }
            }
        }
        while(!s.isEmpty()) {
            Object tmp = s.pop();
            if(!tmp.toString().equals("(")) {
                q.offer(tmp);
            }
        }
        return q;
    }

    /**
     * 传入后缀表达式求值
     * @param queue
     * @return
     */
    public BigDecimal calculate(Queue queue) {
        BigDecimal count = BigDecimal.ZERO;

        List<BigDecimal> num = new ArrayList<>();
        int flag = -1;
        while(!queue.isEmpty() && queue.peek() != null) {
            String a = queue.poll().toString();
            if(!this.isOper(a)) {
                num.add(new BigDecimal(a));
                flag++;
            } else {
                if(a.equals("+")) {
                    count = num.get(flag-1).add(num.get(flag));
                } else if(a.equals("-")) {
                    count = num.get(flag-1).subtract(num.get(flag));
                } else if(a.equals("*")) {
                    count = num.get(flag-1).multiply(num.get(flag));
                } else if(a.equals("/")) {
                    count = num.get(flag-1).divide(num.get(flag));
                }
                num.remove(flag);
                num.remove(flag-1);
                flag--;
                num.add(count);
            }
        }
        return count;
    }

    public static void main(String args[]) {
        Calculator d = new Calculator();
        Queue q = new LinkedList<>();

        Object[] obj = {9, "+", "(", 3, "-", 1, ")", "*", 3, "+", 10, "/", 2};
        Object[] obj2 = {1, "+", 3, "*", 5, "-", 4, "+", "(", 6, "-", 2, ")", "/", 2};
        Object[] obj3 = {"(", "(", 3, "+", 5, ")", "*", 2.5, "+", "(", 5, "-", 6, ")", ")", "+", 3};
        for(int i=0; i<obj3.length; i++) {
            q.offer(obj3[i]);
        }

        System.out.print("长度为" + q.size() + "的中缀表达式:");
        Object[] a = q.toArray();
        for (int i=0; i<a.length; i++) {
            System.out.print(a[i]+" ");
        }

        Queue result = d.poland(q);
        Object[] b = result.toArray();
        System.out.print("---->长度为" + b.length + "的后缀表达式:");
        for (int i=0; i<b.length; i++) {
            System.out.print(b[i]+" ");
        }

        System.out.println();
        System.out.println("q----->:" + result.size());
        BigDecimal count = d.calculate(result);
        System.out.println("计算结果:" + count);
    }
}
