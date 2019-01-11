clear all; close all; clc

%experiment 1
dat = csvread("project3-mahirb-zmcnulty/experimentdata/experiment1.csv", 1,0);

N = dat(:,1);
runtime = dat(:,2);
plot(N, runtime, 'r');
xlabel("Input Size (N)");
ylabel("Runtime (ms)");

print(gcf, '-dpng', 'e1_plot');

%experiment 2
dat = csvread("project3-mahirb-zmcnulty/experimentdata/experiment2.csv", 1,0);

N = dat(:,1);
runtime = dat(:,2);
plot(N, runtime, 'r');
xlabel("K value");
ylabel("Runtime (ms)");

print(gcf, '-dpng', 'e2_plot');

%experiment 3
dat = csvread("project3-mahirb-zmcnulty/experimentdata/experiment3.csv", 1,0);

dict_size = dat(:,1);
rt1 = dat(:,2);
rt2 = dat(:,3);
rt3 = dat(:,4);
plot(dict_size, rt1, 'r'), hold on;
plot(dict_size, rt2), hold on;
plot(dict_size, rt3), hold on;
xlabel("Dictionary Size (N)");
legend({"T1: Constant Hash", "T2: Sum of character values Hash", "T3: Magic 31 Hash"}, 'Location', 'best');
ylabel("Runtime (ms)");

print(gcf, '-dpng', 'e3_plot');