# GeoQuiz
Day 1
创建MainActivity
1.创建textview，显示题目
2.创建before和next按钮，实现切换题目的功能
3.创建cheat按钮，跳转到CheatActivity
4.向CheatActivity传入AnswerIsTrue数据
5.接收来自CheatActivity传入的isShown数据，并赋予一个对象isCheat,让mainActivity可以获取ischeater值


Day 2
创建CheatActivity
1.创建textview，用于显示当前答案
2.创建answer按钮，用于使TextView显示答案
3.CheatActivity接收MainActivity传入的Answer数据，并加以利用
4.实现answer按钮点击功能，当点击时判断当前按钮的值，并向MainActivity传入isShown数据


Day 3
实现判断问题回答是否正确 功能
1.创建True和False按钮，实现判断当前题目对错的功能
2.创建正确题目的数量，并计算总分数值
3.整理，优化函数


Day 4
实现监控问题作弊功能
1.显示按钮的可见性，答过的题按钮为灰色
2.判断本题是否作弊，若作弊显示Toast。
3.限制作弊次数

Day 5
保存数据,防止活动因回收而造成当前数据损失
1.保存题目序号
2.保存CheatActivity 中的 answer值
3.保存当前问题的IsCheater值
4.保存已经作答的问题

