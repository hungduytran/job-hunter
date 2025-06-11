package com.duyhung.jobhunter.service;

import com.duyhung.jobhunter.domain.Job;
import com.duyhung.jobhunter.domain.Skill;
import com.duyhung.jobhunter.domain.Subscriber;
import com.duyhung.jobhunter.domain.response.email.ResEmailJob;
import com.duyhung.jobhunter.repository.JobRepository;
import com.duyhung.jobhunter.repository.SkillRepository;
import com.duyhung.jobhunter.repository.SubscriberRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository, JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean existsByEmail(String email) {
        return subscriberRepository.existsByEmail(email);
    }

    public Subscriber createSubscriber(Subscriber subscriber) {
        //check skill
        if (subscriber.getSkills() != null) {
            List<Long> skills = subscriber.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(skills);
            subscriber.setSkills(dbSkills);
        }
        return subscriberRepository.save(subscriber);
    }

    public Subscriber findById(long id) {
        return subscriberRepository.findById(id);
    }

    public Subscriber updateSubscriber(Subscriber subsDB, Subscriber subsRequest) {
        //ckeck skill
        if (subsRequest.getSkills() != null) {
            List<Long> reqSkills = subsRequest.getSkills()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSKills = this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(dbSKills);
        }
        return this.subscriberRepository.save(subsDB);
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {
                         List<ResEmailJob> arr = listJobs.stream().map(
                         job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

//    @Scheduled(cron = "*/10 * * * * *")
//    public void testCron() {
//        System.out.println(">>> TEST CRON");
//    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));
        List<Skill> skills = job.getSkills();
        List<ResEmailJob.SkillEmail> s = skills.stream().map(skill -> new ResEmailJob.SkillEmail(skill.getName()))
                .collect(Collectors.toList());
        res.setSkills(s);
        return res;
    }

    public Subscriber findByEmail(String email) {
        return subscriberRepository.findByEmail(email);
    }
}
