close all; clc

dat = csvread("~/Desktop/CSE/373/HW/HW2/project1-mahirb-zmcnulty/experimentdata/experiment1.csv", 1, 0);
dict_sizes = dat(:,1);
test1_results = dat(:,2);
test2_results = dat(:,3);
%%
close all;
plot(dict_sizes, test1_results, 'r.');
hold on;
plot(dict_sizes, test2_results, 'b.');
title("Experiment 1 Results - Remove Runtime Analysis")
xlabel("Input Dictionary Size (N)");
ylabel("run time (ms)");
legend({"test 1: remove forwards", "test 2: remove backwards"}, 'Fontsize', 20, 'Location', 'northwest');
set(gca, 'Fontsize', 15)

print(gcf,'-dpng','~/Desktop/experiment1_graph.png');

%% experiment 2

dat2 = csvread("~/Desktop/CSE/373/HW/HW2/project1-mahirb-zmcnulty/experimentdata/experiment2.csv", 1, 0);
dict_sizes = dat2(:,1);
test1 = dat2(:,2);
test2 = dat2(:,3);
test3 = dat2(:,4);
%%

plot(dict_sizes, test1, dict_sizes, test2, dict_sizes, test3, 'Linewidth', 4);
title("Experiment 2 Results - List Traversal Run-time Analysis")
xlabel("Input Dictionary Size (N)");
ylabel("run time (ms)");
legend({"test 1: get() traversal", "test 2: iterator", "test 3: for each loop"}, 'Fontsize', 20, 'Location', 'northwest');
set(gca, 'Fontsize', 15)
print(gcf,'-dpng','~/Desktop/experiment2_graph.png');